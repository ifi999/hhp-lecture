package com.hhp.lecture.controller;

import com.hhp.lecture.application.UserLectureService;
import com.hhp.lecture.common.ApiResponse;
import com.hhp.lecture.controller.dto.CreateApplyLectureRequest;
import com.hhp.lecture.controller.dto.CreateApplyLectureResponse;
import com.hhp.lecture.domain.UserLecture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        final UserLecture userLecture = userLectureService.applyLecture(new UserLecture(request.getUserId(), request.getLectureId()), request.getAppliedDate());

        return ApiResponse.isOk(CreateApplyLectureResponse.from(userLecture));
    }

}
