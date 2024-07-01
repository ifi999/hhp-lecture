package com.hhp.lecture.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@ToString
public class Lecture {

    private long id;
    private String lectureName;
    private LocalDateTime applyDate;
    private LocalDateTime openDate;
    private int appliedCount;

    private static final int MAX_PARTICIPANTS = 30;

    public Lecture(
        final long id,
        final String lectureName,
        final LocalDateTime applyDate,
        final LocalDateTime openDate,
        final int appliedCount
    ) {
        Assert.isTrue(id > 0, "The ID must be positive.");
        Assert.notNull(lectureName, "The lecture's name cannot be null.");
        Assert.notNull(applyDate, "The lecture's apply date cannot be null.");
        Assert.notNull(openDate, "The lecture's open date cannot be null.");
        Assert.isTrue(appliedCount >= 0, "The applied count must be greater than zero.");

        this.id = id;
        this.lectureName = lectureName;
        this.applyDate = applyDate;
        this.openDate = openDate;
        this.appliedCount = appliedCount;
    }

    public void validateLectureApplication(final LocalDateTime applicationDate) {
        validateApplicationDate(applicationDate);
        validateApplyCount();
    }

    private void validateApplicationDate(final LocalDateTime applicationDate) {
        if (applicationDate.isBefore(this.applyDate)) {
            throw new IllegalArgumentException("Invalid application date: " + applicationDate + ". Application date must be after " + this.applyDate + ".");
        }
    }

    private void validateApplyCount() {
        if (this.appliedCount < 0) {
            throw new IllegalStateException("Invalid applied count: " + appliedCount + ". Lecture ID: " + this.id);
        }

        if (this.appliedCount - MAX_PARTICIPANTS >= 0) {
            throw new IllegalStateException("The number of participants has already exceeded the maximum allowed limit of " + MAX_PARTICIPANTS + " participants.");
        }
    }

}
