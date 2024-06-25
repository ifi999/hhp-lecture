package com.hhp.lecture.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@ToString
public class UserLecture {

    private long userId;
    private long lectureId;
    private String lectureName;
    private LocalDateTime openDate;
    private boolean isEnrolled;

    public UserLecture(final long userId, final long lectureId) {
        Assert.isTrue(userId > 0, "The user's ID must be positive.");
        Assert.isTrue(lectureId > 0, "The lecture's ID must be positive.");

        this.userId = userId;
        this.lectureId = lectureId;
    }

    @Builder
    public UserLecture(
        final long userId,
        final long lectureId,
        final String lectureName,
        final LocalDateTime openDate,
        final boolean isEnrolled
    ) {
        Assert.isTrue(userId > 0, "The user's ID must be positive.");
        Assert.isTrue(lectureId > 0, "The lecture's ID must be positive.");
        Assert.notNull(openDate, "The lecture's open date cannot be null.");
        Assert.notNull(lectureName, "The lecture's name cannot be null.");

        this.userId = userId;
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.openDate = openDate;
        this.isEnrolled = isEnrolled;
    }

}
