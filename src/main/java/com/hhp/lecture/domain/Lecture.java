package com.hhp.lecture.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class Lecture {

    private long id;
    private String lectureName;
    private int appliedCount;

    public Lecture(final long id, final String lectureName, final int appliedCount) {
        Assert.isTrue(id > 0, "The ID must be positive.");
        Assert.notNull(lectureName, "The lecture's name cannot be null.");
        Assert.isTrue(appliedCount >= 0, "The applied count must be greater than zero.");

        this.id = id;
        this.lectureName = lectureName;
        this.appliedCount = appliedCount;
    }

    public void incrementAppliedCount() {
        this.appliedCount++;
    }

}
