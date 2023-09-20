package com.livable.server.invitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.entity.*;
import com.livable.server.invitation.domain.InvitationErrorCode;
import com.livable.server.invitation.domain.InvitationPurpose;
import com.livable.server.invitation.dto.InvitationDetailTimeDto;
import com.livable.server.invitation.dto.InvitationRequest;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.repository.InvitationRepository;
import com.livable.server.invitation.repository.InvitationReservationMapRepository;
import com.livable.server.invitation.repository.OfficeRepository;
import com.livable.server.member.repository.MemberRepository;
import com.livable.server.reservation.repository.ReservationRepository;
import com.livable.server.visitation.domain.VisitationErrorCode;
import com.livable.server.visitation.dto.VisitationResponse;
import com.livable.server.visitation.repository.ParkingLogRepository;
import com.livable.server.visitation.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InvitationService {
    private static final int INTERVIEW_MAXIMUM_COUNT = 1;
    private static final int INVITATION_MAXIMUM_COUNT = 30;

    private final MemberRepository memberRepository;
    private final OfficeRepository officeRepository;
    private final InvitationRepository invitationRepository;
    private final ReservationRepository reservationRepository;
    private final InvitationReservationMapRepository invitationReservationMapRepository;
    private final VisitorRepository visitorRepository;
    private final ParkingLogRepository parkingLogRepository;

    public VisitationResponse.InvitationTimeDto findInvitationTime(Long visitorId) {
        InvitationDetailTimeDto invitationDetailTimeDto = invitationRepository.findInvitationDetailTimeByVisitorId(visitorId)
                .orElseThrow(() -> new GlobalRuntimeException(VisitationErrorCode.NOT_FOUND));

        return VisitationResponse.InvitationTimeDto.from(invitationDetailTimeDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Success<InvitationResponse.AvailablePlacesDTO>> getAvailablePlaces(Long memberId) {
        // 1. memberId가 속한 companyId를 가져옴
        Long companyId = getCompanyIdByMemberId(memberId);

        // 2. company 에 속해있는 사무실 리스트를 가져옴
        List<Office> officeEntities = officeRepository.findAllByCompanyId(companyId);

        // 3. company 가 예약한 공용 공간 리스트를 가져옴
        List<InvitationResponse.ReservationDTO> reservations = reservationRepository
                .findReservationsByCompanyId(companyId);

        // 4. ReservationDTO 를 연속된 시간은 하나로 합치는 작업을 진행
        combineConsecutiveReservation(reservations);

        // 5. DTO 변환 작업
        List<InvitationResponse.OfficeDTO> offices = officeEntities.stream()
                .map(InvitationResponse.OfficeDTO::from).collect(Collectors.toList());

        List<InvitationResponse.CommonPlaceDTO> commonPlaces = reservations.stream()
                .map(InvitationResponse.CommonPlaceDTO::from).collect(Collectors.toList());

        InvitationResponse.AvailablePlacesDTO responseBody = InvitationResponse.AvailablePlacesDTO.builder()
                .offices(offices)
                .commonPlaces(commonPlaces)
                .build();

        return ApiResponse.success(responseBody, HttpStatus.OK);
    }

    private void combineConsecutiveReservation(List<InvitationResponse.ReservationDTO> reservations) {
        Iterator<InvitationResponse.ReservationDTO> reservationsIterator = reservations.iterator();
        InvitationResponse.ReservationDTO beforeReservation = null;
        while (reservationsIterator.hasNext()) {
            InvitationResponse.ReservationDTO currentReservation = reservationsIterator.next();

            if (isNotCombineTarget(beforeReservation, currentReservation)) {
                beforeReservation = currentReservation;
                continue;
            }
            beforeReservation.setReservationEndTime(currentReservation.getReservationEndTime());
            reservationsIterator.remove();
        }
    }

    private boolean isNotCombineTarget(
            InvitationResponse.ReservationDTO before,
            InvitationResponse.ReservationDTO current
    ) {
        // null 이거나 commonPlaceId가 다르거나, 날짜가 다르거나, 연속된 시간이 아닌 경우에는 시간을 합치는 목표가 아님
        return before == null
                || !before.getCommonPlaceId().equals(current.getCommonPlaceId())
                || !before.getReservationDate().equals(current.getReservationDate())
                || !before.getReservationEndTime().equals(current.getReservationStartTime());
    }

    private Long getCompanyIdByMemberId(Long memberId) {
        Member member = checkExistMemberById(memberId);

        return member.getCompany().getId();
    }

    @Transactional
    public ResponseEntity<?> createInvitation(InvitationRequest.CreateDTO dto, Long memberId) {
        checkInterviewVisitorCount(dto);

        Member member = checkExistMemberById(memberId);
        Invitation invitation = createInvitation(dto, member);
        createVisitors(dto.getVisitors(), invitation);
        reserveCommonPlaces(dto, invitation);

        return ApiResponse.success(HttpStatus.CREATED);
    }

    /* 면접의 경우에는 1명만 초대 가능 */
    private void checkInterviewVisitorCount(InvitationRequest.CreateDTO dto) {
        if (dto.getPurpose().equals(InvitationPurpose.INTERVIEW.getValue())
                && dto.getVisitors().size() > INTERVIEW_MAXIMUM_COUNT) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_INTERVIEW_MAXIMUM_NUMBER);
        }
    }

    private Member checkExistMemberById(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        return memberOptional.orElseThrow(() -> new GlobalRuntimeException(InvitationErrorCode.MEMBER_NOT_EXIST));
    }

    private Invitation createInvitation(InvitationRequest.CreateDTO dto, Member member) {
        Invitation invitation = dto.toEntity(member);

        return invitationRepository.save(invitation);
    }

    private void createVisitors(List<InvitationRequest.VisitorCreateDTO> visitorCreateDTOS, Invitation invitation) {
        for (InvitationRequest.VisitorCreateDTO visitorCreateDTO : visitorCreateDTOS) {
            Visitor visitor = visitorCreateDTO.toEntity(invitation);
            visitorRepository.save(visitor);
        }
    }

    private void reserveCommonPlaces(InvitationRequest.CreateDTO dto, Invitation invitation) {
        LocalDateTime startDateTime = dto.getStartDate();
        LocalDateTime endDateTime = dto.getEndDate();
        checkDateTimeValidate(startDateTime, endDateTime);

        if (isReservedCommonPlace(dto.getCommonPlaceId())) {
            int expectedReservationCount = getExpectedReservationCount(startDateTime, endDateTime);
            List<Reservation> reservations = reservationRepository
                    .findReservationsByCommonPlaceIdAndStartDateAndEndDate(dto.getCommonPlaceId(), startDateTime, endDateTime);

            checkReservationCount(reservations, expectedReservationCount);
            createInvitationReservationMap(reservations, invitation);
        }
    }

    private void checkDateTimeValidate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (endDateTime.toLocalDate().isBefore(startDateTime.toLocalDate())) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_DATE);
        }
        if (!endDateTime.toLocalTime().isAfter(startDateTime.toLocalTime())) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_TIME);
        }
        checkTimeUnitValidation(startDateTime.toLocalTime(), endDateTime.toLocalTime());
    }

    private void checkTimeUnitValidation(LocalTime startTime, LocalTime endTime) {
        int startMinute = startTime.getMinute();
        int endMinute = endTime.getMinute();

        if ((startMinute % 30 != 0) || (endMinute % 30 != 0)) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_TIME_UNIT);
        }
    }

    private boolean isReservedCommonPlace(Long commonPlaceId) {
        return commonPlaceId != null;
    }

    /* 입력된 시간 범위 내에서 반드시 존재해야 하는 예약 정보 개수를 반환 */
    private int getExpectedReservationCount(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        int dayCount = (int) Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1;
        int timeCount = (endTime.toSecondOfDay() - startTime.toSecondOfDay()) / 1800;

        return dayCount * timeCount;
    }

    /* 입력된 시작, 종료 날짜에 대한 예약 정보 개수가 예상한 값과 맞는지 확인 */
    private void checkReservationCount(List<Reservation> reservations, int count) {
        if (reservations.size() != count) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_RESERVATION_COUNT);
        }
    }

    private void createInvitationReservationMap(List<Reservation> reservations, Invitation invitation) {
        for (Reservation reservation : reservations) {
            InvitationReservationMap invitationReservationMap = InvitationReservationMap.builder()
                    .invitation(invitation)
                    .reservation(reservation)
                    .build();

            invitationReservationMapRepository.save(invitationReservationMap);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Success<List<InvitationResponse.ListDTO>>> getInvitations(Long memberId) {
        checkExistMemberById(memberId);
        List<InvitationResponse.ListDTO> invitationDTOs = invitationRepository.findInvitationsByMemberId(memberId);

        return ApiResponse.success(invitationDTOs, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Success<InvitationResponse.DetailDTO>> getInvitation(Long invitationId, Long memberId) {
        checkExistMemberById(memberId);
        checkInvitationOwner(invitationId, memberId);

        InvitationResponse.DetailDTO invitationDTO
                = invitationRepository.findInvitationAndVisitorsByInvitationId(invitationId);

        return ApiResponse.success(invitationDTO, HttpStatus.OK);
    }

    private void checkInvitationOwner(Long invitationId, Long memberId) {
        if (invitationRepository.countByIdAndMemberId(invitationId, memberId).equals(0L)) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_INVITATION_OWNER);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteInvitation(Long invitationId, Long memberId) {
        checkExistMemberById(memberId);
        checkInvitationOwner(invitationId, memberId);

        // Step 1. 초대장 가져옴
        Optional<Invitation> invitationOptional = invitationRepository.findById(invitationId);
        Invitation invitation = invitationOptional
                .orElseThrow(() -> new GlobalRuntimeException(InvitationErrorCode.INVITATION_NOT_EXIST));

        // Step 2. 초대장 방문날짜 확인
        checkInvitationStartDate(invitation);

        // Step 3. 예약된 장소에 대한 예약 정보 제거
        deleteReservationsByInvitation(invitation);

        // Step 4. 초대장에 등록된 방문자 데이터 + 주차 등록 데이터 삭제
        deleteVisitorsAndParkingLogByInvitation(invitation);

        // Step 5. 초대장 삭제
        invitationRepository.delete(invitation);

        return ApiResponse.success(HttpStatus.OK);
    }

    private void checkInvitationStartDate(Invitation invitation) {
        if (invitation.getStartDate().isBefore(LocalDate.now())) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_DELETE_DATE);
        }
    }

    private void deleteReservationsByInvitation(Invitation invitation) {
        invitationReservationMapRepository.deleteAllByInvitationId(invitation.getId());
    }

    private void deleteVisitorsAndParkingLogByInvitation(Invitation invitation) {
        List<Visitor> visitors = visitorRepository.findVisitorsByInvitation(invitation);
        List<Long> visitorsIds = visitors.stream().map(Visitor::getId).collect(Collectors.toList());

        parkingLogRepository.deleteByVisitorIdsIn(visitorsIds);
        visitorRepository.deleteByIdsIn(visitorsIds);
    }

    @Transactional
    public ResponseEntity<?> updateInvitation(Long invitationId, InvitationRequest.UpdateDTO dto, Long memberId) {
        checkExistMemberById(memberId);
        checkInvitationOwner(invitationId, memberId);

        Optional<Invitation> invitationOptional = invitationRepository.findById(invitationId);
        Invitation invitation = invitationOptional
                .orElseThrow(() -> new GlobalRuntimeException(InvitationErrorCode.INVITATION_NOT_EXIST));

        checkInvitationStartDate(invitation);
        checkModifiedCommonPlaceId(invitation, dto);

        boolean shouldSendToAlreadyVisitor = false;
        boolean shouldSendToAddedVisitor = checkAddedVisitorsCount(invitation, dto);

        if (isModifiedInvitationDateTime(invitation, dto)) {
            shouldSendToAlreadyVisitor = true;
            if (isReservedCommonPlace(dto.getCommonPlaceId())) {

                invitationReservationMapRepository.deleteAllByInvitationId(invitation.getId());
                reserveNewCommonPlaces(dto, invitation);
            }
        }

        invitation.updateDateTime(dto.getStartDate(), dto.getEndDate());
        invitation.updateDescription(dto.getDescription());

        if (shouldSendToAlreadyVisitor) {
            List<Visitor> currentVisitors = visitorRepository.findVisitorsByInvitation(invitation);

            // TODO: 기존 등록되어 있던 방문자들에게 알림톡을 다시 보내는 로직 추가
        }

        if (shouldSendToAddedVisitor) {
            List<Visitor> visitors = dto.getVisitors().stream()
                    .map(visitor -> visitor.toEntity(invitation)).collect(Collectors.toList());

            visitorRepository.saveAll(visitors);

            // TODO: 새로 등록된 방문자들에게 알림톡을 다시 보내는 로직 추가
        }

        return ApiResponse.success(HttpStatus.OK);
    }

    private boolean checkAddedVisitorsCount(Invitation invitation, InvitationRequest.UpdateDTO dto) {
        if (invitation.getPurpose().equals(InvitationPurpose.INTERVIEW.getValue())) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_INTERVIEW_MAXIMUM_NUMBER);
        }

        long alreadyCount = visitorRepository.countByInvitation(invitation);
        long addedCount = dto.getVisitors().size();

        if (alreadyCount + addedCount > INVITATION_MAXIMUM_COUNT) {
            throw new GlobalRuntimeException(InvitationErrorCode.INVALID_INVITATION_MAXIMUM_NUMBER);
        }

        return dto.getVisitors().size() != 0;
    }

    private boolean isModifiedInvitationDateTime(Invitation invitation, InvitationRequest.UpdateDTO dto) {
        return !LocalDateTime.of(invitation.getStartDate(), invitation.getStartTime()).isEqual(dto.getStartDate())
                || !LocalDateTime.of(invitation.getEndDate(), invitation.getEndTime()).isEqual(dto.getEndDate());
    }

    private void checkModifiedCommonPlaceId(Invitation invitation, InvitationRequest.UpdateDTO dto) {
        Long currentCommonPlaceId = invitationRepository.getCommonPlaceIdByInvitationId(invitation.getId());

        if (!currentCommonPlaceId.equals(dto.getCommonPlaceId())) {
            throw new GlobalRuntimeException(InvitationErrorCode.CAN_NOT_CHANGED_COMMON_PLACE_OF_INVITATION);
        }
    }

    private void reserveNewCommonPlaces(InvitationRequest.UpdateDTO dto, Invitation invitation) {
        LocalDateTime startDateTime = dto.getStartDate();
        LocalDateTime endDateTime = dto.getEndDate();
        checkDateTimeValidate(startDateTime, endDateTime);

        int expectedReservationCount = getExpectedReservationCount(startDateTime, endDateTime);
        List<Reservation> reservations = reservationRepository
                .findReservationsByCommonPlaceIdAndStartDateAndEndDate(dto.getCommonPlaceId(), startDateTime, endDateTime);

        checkReservationCount(reservations, expectedReservationCount);
        createInvitationReservationMap(reservations, invitation);
    }
}
