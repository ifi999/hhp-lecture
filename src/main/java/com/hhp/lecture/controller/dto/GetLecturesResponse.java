package com.hhp.lecture.controller.dto;

import com.hhp.lecture.domain.Lecture;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetLecturesResponse {

    private long id;
    private String lectureName;
    private LocalDateTime applyDate;
    private LocalDateTime openDate;
    private int appliedCount;

    public GetLecturesResponse(
        final long id,
        final String lectureName,
        final LocalDateTime applyDate,
        final LocalDateTime openDate,
        final int appliedCount
    ) {
        this.id = id;
        this.lectureName = lectureName;
        this.applyDate = applyDate;
        this.openDate = openDate;
        this.appliedCount = appliedCount;
    }

    public static List<GetLecturesResponse> from(List<Lecture> lectures) {
        return lectures.stream()
            .map(o -> new GetLecturesResponse(
                o.getId(),
                o.getLectureName(),
                o.getApplyDate(),
                o.getOpenDate(),
                o.getAppliedCount()
            ))
            .toList();
    }

}
