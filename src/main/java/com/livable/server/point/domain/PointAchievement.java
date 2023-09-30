package com.livable.server.point.domain;

import com.livable.server.entity.PointCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PointAchievement {

    DAY07(7, 100, PointCode.PA03),
    DAY14(14, 100, PointCode.PA04),
    DAY21(21, 100, PointCode.PA05),
    DAY28(28, 100, PointCode.PA06);

    private final Integer dateCount;
    private final Integer amount;
    private final PointCode pointCode;

    public static final List<Integer> DAY_COUNTS;
    public static final List<PointCode> POINT_CODES;

    static {
        DAY_COUNTS = Arrays.stream(PointAchievement.values())
                .map(PointAchievement::getDateCount)
                .collect(Collectors.toList());

        POINT_CODES = Arrays.stream(PointAchievement.values())
                .map(PointAchievement::getPointCode)
                .collect(Collectors.toList());
    }

    public static PointAchievement valueOf(Integer count) throws InputMismatchException, IllegalArgumentException {
        if (!DAY_COUNTS.contains(count)) {
            throw new InputMismatchException();
        }

        for (PointAchievement pointAchievement : PointAchievement.values()) {
            Integer dateCount = pointAchievement.getDateCount();
            if (dateCount.equals(count)) {
                return pointAchievement;
            }
        }
        throw new IllegalArgumentException();
    }
}
