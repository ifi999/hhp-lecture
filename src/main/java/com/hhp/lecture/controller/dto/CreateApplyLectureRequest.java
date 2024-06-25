package com.hhp.lecture.controller.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateApplyLectureRequest {

    private long userId;
    private long lectureId;

    public CreateApplyLectureRequest(final long userId, final long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }

}
