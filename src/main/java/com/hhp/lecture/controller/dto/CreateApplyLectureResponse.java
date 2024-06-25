package com.hhp.lecture.controller.dto;

import com.hhp.lecture.domain.UserLecture;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateApplyLectureResponse {

    private long userId;
    private long lectureId;
    private String lectureName;
    private LocalDateTime openDate;
    private boolean isEnrolled;

    public CreateApplyLectureResponse(
        final long userId,
        final long lectureId,
        final String lectureName,
        final LocalDateTime openDate,
        final boolean isEnrolled
    ) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.openDate = openDate;
        this.isEnrolled = isEnrolled;
    }

    public static CreateApplyLectureResponse from(final UserLecture userLecture){
        return new CreateApplyLectureResponse(
            userLecture.getUserId(),
            userLecture.getLectureId(),
            userLecture.getLectureName(),
            userLecture.getOpenDate(),
            userLecture.isEnrolled()
        );
    }

}
