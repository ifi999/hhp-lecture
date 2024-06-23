package com.hhp.lecture.controller.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CreateApplyLectureRequest {

    private long userId;
    private long lectureId;
    private LocalDateTime appliedDate;

    public CreateApplyLectureRequest(final long userId, final long lectureId, final LocalDateTime appliedDate) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.appliedDate = appliedDate;
    }

}
