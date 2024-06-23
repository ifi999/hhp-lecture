package com.hhp.lecture.application;

import com.hhp.lecture.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserLectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final ApplyHistoryRepository applyHistoryRepository;
    private final UserLectureRepository userLectureRepository;

    private static final LocalDateTime OPEN_DATETIME = LocalDateTime.of(2024, 4, 20, 13, 20);
    private static final int MAX_PARTICIPANTS = 30;

    public UserLectureService(
        final LectureRepository lectureRepository,
        final UserRepository userRepository,
        final ApplyHistoryRepository applyHistoryRepository,
        final UserLectureRepository userLectureRepository
    ) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.applyHistoryRepository = applyHistoryRepository;
        this.userLectureRepository = userLectureRepository;
    }

    @Transactional
    public UserLecture applyLecture(final UserLecture userLecture, final LocalDateTime appliedDate) {
        final User user = userRepository.getUserByUserId(userLecture.getUserId());
        final Lecture lecture = lectureRepository.getLectureByLectureId(userLecture.getLectureId());

        final boolean isValidApplication = isApplyLectureValid(appliedDate, user, lecture);
        if (!isValidApplication) {
            return UserLecture.builder()
                .userId(user.getId())
                .lectureId(lecture.getId())
                .lectureName(lecture.getLectureName())
                .isEnrolled(false)
                .build();
        }

        lectureRepository.updateLecture(lecture);
        applyHistoryRepository.saveApplyHistory(new ApplyHistory(user.getId(), lecture.getId()));
        userLectureRepository.saveUserLecture(user, lecture);

        return UserLecture.builder()
            .userId(user.getId())
            .lectureId(lecture.getId())
            .lectureName(lecture.getLectureName())
            .isEnrolled(true)
            .build();
    }

    private boolean isApplyLectureValid(
        final LocalDateTime appliedDate,
        final User user,
        final Lecture lecture
    ) {
        if (appliedDate.isBefore(OPEN_DATETIME)) {
            throw new IllegalArgumentException("Invalid application date: " + appliedDate + ". Application date must be before " + OPEN_DATETIME + ".");
        }

        final boolean isApplied = userLectureRepository.existsByUserAndLecture(user, lecture);
        if (isApplied) {
            throw new IllegalArgumentException("Already applied lecture. User Id: " + user.getId() + ", Lecture ID: " + lecture.getId() + ".");
        }

        final int appliedCount = lecture.getAppliedCount();
        if (appliedCount < 0) {
            throw new IllegalStateException("Invalid applied count: " + appliedCount + ". Lecture ID: " + lecture.getId());
        }

        if (appliedCount - MAX_PARTICIPANTS >= 0) {
            return false;
        }

        return true;
    }

}
