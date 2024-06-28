package com.hhp.lecture.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class ApplyHistory {

    private long id;
    private long userId;
    private long lectureId;

    public ApplyHistory(final long userId, final long lectureId) {
        Assert.isTrue(userId > 0, "The user's ID must be positive.");
        Assert.isTrue(lectureId > 0, "The lecture's ID must be positive.");

        this.userId = userId;
        this.lectureId = lectureId;
    }

    @Builder
    public ApplyHistory(final long id, final long userId, final long lectureId) {
        Assert.isTrue(id > 0, "The ID must be positive.");
        Assert.isTrue(userId > 0, "The User ID must be positive.");
        Assert.isTrue(lectureId > 0, "The Lecture ID must be positive.");

        this.id = id;
        this.userId = userId;
        this.lectureId = lectureId;
    }

}
