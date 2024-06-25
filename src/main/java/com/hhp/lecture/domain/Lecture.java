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

    public Lecture(
        final long id,
        final String lectureName,
        final LocalDateTime applyDate,
        final LocalDateTime openDate,
        final int appliedCount
    ) {
        Assert.isTrue(id > 0, "The ID must be positive.");
        Assert.notNull(lectureName, "The lecture's name cannot be null.");
        Assert.notNull(applyDate, "The lecture's apply date cannot be null");
        Assert.notNull(applyDate, "The lecture's open date cannot be null");
        Assert.isTrue(appliedCount >= 0, "The applied count must be greater than zero.");

        this.id = id;
        this.lectureName = lectureName;
        this.applyDate = applyDate;
        this.openDate = openDate;
        this.appliedCount = appliedCount;
    }

    public void incrementAppliedCount() {
        this.appliedCount++;
    }

}
