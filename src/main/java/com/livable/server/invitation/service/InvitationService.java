package com.livable.server.invitation.service;

import com.livable.server.core.exception.GlobalRuntimeException;
import com.livable.server.core.response.ApiResponse;
import com.livable.server.core.response.ApiResponse.Success;
import com.livable.server.entity.Member;
import com.livable.server.entity.Office;
import com.livable.server.invitation.domain.InvitationErrorCode;
import com.livable.server.invitation.dto.InvitationProjection;
import com.livable.server.invitation.dto.InvitationResponse;
import com.livable.server.invitation.repository.MemberRepository;
import com.livable.server.invitation.repository.OfficeRepository;
import com.livable.server.invitation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InvitationService {

    private final MemberRepository memberRepository;
    private final OfficeRepository officeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Success<InvitationResponse.AvailablePlacesDTO>> getAvailablePlaces(Long memberId) {
        // 1. memberId가 속한 companyId를 가져옴
        Long companyId = getCompanyIdByMemberId(memberId);

        // 2. company 에 속해있는 사무실 리스트를 가져옴
        List<Office> officeEntities = officeRepository.findAllByCompanyId(companyId);

        // 3. company 가 예약한 공용 공간 리스트를 가져옴
        List<InvitationProjection.ReservationDTO> reservations = reservationRepository
                .findReservationsByCompanyId(companyId);

        // 4. ReservationDTO 를 연속된 시간은 하나로 합치는 작업을 진행
        combineConsecutiveReservation(reservations);

        // 5. DTO 변환 작업
        List<InvitationResponse.OfficeDTO> officeDTOList = officeEntities.stream()
                .map(InvitationResponse.OfficeDTO::from).collect(Collectors.toList());

        List<InvitationResponse.CommonPlaceDTO> commonPlaces = reservations.stream()
                .map(InvitationResponse.CommonPlaceDTO::from).collect(Collectors.toList());

        return ApiResponse.success(new InvitationResponse.AvailablePlacesDTO(officeDTOList, commonPlaces), HttpStatus.OK);
    }

    private void combineConsecutiveReservation(List<InvitationProjection.ReservationDTO> reservations) {
        Iterator<InvitationProjection.ReservationDTO> reservationsIterator = reservations.iterator();
        InvitationProjection.ReservationDTO beforeReservation = null;
        while (reservationsIterator.hasNext()) {
            InvitationProjection.ReservationDTO currentReservation = reservationsIterator.next();

            if (isNotCombineTarget(beforeReservation, currentReservation)) {
                beforeReservation = currentReservation;
                continue;
            }
            beforeReservation.setReservationEndTime(currentReservation.getReservationEndTime());
            reservationsIterator.remove();
        }
    }

    private boolean isNotCombineTarget(
            InvitationProjection.ReservationDTO before,
            InvitationProjection.ReservationDTO current
    ) {
        // null 이거나 commonPlaceId가 다르거나, 날짜가 다르거나, 연속된 시간이 아닌 경우에는 시간을 합치는 목표가 아님
        return before == null
                || !before.getCommonPlaceId().equals(current.getCommonPlaceId())
                || !before.getReservationDate().equals(current.getReservationDate())
                || !before.getReservationEndTime().equals(current.getReservationStartTime());
    }

    private Long getCompanyIdByMemberId(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.orElseThrow(() -> new GlobalRuntimeException(InvitationErrorCode.MEMBER_NOT_EXIST));

        return member.getCompany().getId();
    }

}
