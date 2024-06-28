package com.hhp.lecture.controller.dto;

import com.hhp.lecture.domain.UserLecture;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetAppliedLecturesResponse {

    private long userId;
    private long lectureId;
    private String lectureName;
    private LocalDateTime openDate;

    public GetAppliedLecturesResponse(
        final long userId,
        final long lectureId,
        final String lectureName,
        final LocalDateTime openDate
    ) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.openDate = openDate;
    }

    public static List<GetAppliedLecturesResponse> from(List<UserLecture> userLectures) {
        return userLectures.stream()
            .map(o -> new GetAppliedLecturesResponse(
                o.getUserId(),
                o.getLectureId(),
                o.getLectureName(),
                o.getOpenDate()
            ))
            .toList();
    }

}
