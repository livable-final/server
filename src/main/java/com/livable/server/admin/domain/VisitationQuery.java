package com.livable.server.admin.domain;

import com.livable.server.core.exception.GlobalRuntimeException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class VisitationQuery {

    private VisitationQueryCondition queryCondition;
    private String query;
    private LocalDate startDate;
    private LocalDate endDate;

    public void setDefaultDate() {
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
    }

    public void validate() {
        if (startDate == null && endDate == null) {
            setDefaultDate();
            return;
        }

        if (startDate != null && endDate != null) {
            checkDateTime(startDate, endDate);
        }
    }

    private void checkDateTime(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
            throw new GlobalRuntimeException(AdminErrorCode.INVALID_QUERY);
        }
    }
}
