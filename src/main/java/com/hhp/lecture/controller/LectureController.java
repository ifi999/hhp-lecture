package com.hhp.lecture.controller;

import com.hhp.lecture.application.UserLectureService;
import com.hhp.lecture.common.ApiResponse;
import com.hhp.lecture.controller.dto.CreateApplyLectureRequest;
import com.hhp.lecture.controller.dto.CreateApplyLectureResponse;
import com.hhp.lecture.controller.dto.GetAppliedLecturesResponse;
import com.hhp.lecture.controller.dto.GetLecturesResponse;
import com.hhp.lecture.domain.Lecture;
import com.hhp.lecture.domain.UserLecture;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/lectures")
@RestController
public class LectureController {

    private final UserLectureService userLectureService;

    public LectureController(final UserLectureService userLectureService) {
        this.userLectureService = userLectureService;
    }

    @PostMapping("/apply")
    public ApiResponse<CreateApplyLectureResponse> applyLecture(
            @RequestBody final CreateApplyLectureRequest request
    ) {
        final UserLecture userLecture = userLectureService.applyLecture(new UserLecture(request.getUserId(), request.getLectureId()));

        return ApiResponse.isOk(CreateApplyLectureResponse.from(userLecture));
    }

    @GetMapping
    public ApiResponse<List<GetLecturesResponse>> getLectures() {
        final List<Lecture> lectures = userLectureService.getLectures();

        return ApiResponse.isOk(GetLecturesResponse.from(lectures));
    }

    @GetMapping("/application/{userId}")
    public ApiResponse<List<GetAppliedLecturesResponse>> getAppliedLectures(
        @PathVariable final long userId
    ) {
        System.out.println("userId = " + userId);
        final List<UserLecture> appliedLectures = userLectureService.getAppliedLectures(userId);

        return ApiResponse.isOk(GetAppliedLecturesResponse.from(appliedLectures));
    }

}
