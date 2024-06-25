package com.hhp.lecture.application;

import com.hhp.lecture.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class UserLectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final ApplyHistoryRepository applyHistoryRepository;
    private final UserLectureRepository userLectureRepository;
    private final DateTimeProvider dateTimeProvider;

    private static final int MAX_PARTICIPANTS = 30;

    public UserLectureService(
        final LectureRepository lectureRepository,
        final UserRepository userRepository,
        final ApplyHistoryRepository applyHistoryRepository,
        final UserLectureRepository userLectureRepository,
        final DateTimeProvider dateTimeProvider
    ) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.applyHistoryRepository = applyHistoryRepository;
        this.userLectureRepository = userLectureRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    public UserLecture applyLecture(final UserLecture userLecture) {
        final User user = userRepository.getUserByUserId(userLecture.getUserId());
        final Lecture lecture = lectureRepository.getLectureByLectureId(userLecture.getLectureId());
        final LocalDateTime now = dateTimeProvider.now();

        final boolean isValidApplication = isApplyLectureValid(now, user, lecture);
        if (!isValidApplication) {
            return convertToUserLecture(user, lecture, false);
        }

        lectureRepository.updateLecture(lecture);
        applyHistoryRepository.saveApplyHistory(new ApplyHistory(user.getId(), lecture.getId()));
        userLectureRepository.saveUserLecture(user, lecture, now);

        return convertToUserLecture(user, lecture, true);
    }

    private boolean isApplyLectureValid(
        final LocalDateTime appliedDate,
        final User user,
        final Lecture lecture
    ) {
        if (appliedDate.isBefore(lecture.getApplyDate())) {
            throw new IllegalArgumentException("Invalid application date: " + appliedDate + ". Application date must be before " + lecture.getApplyDate() + ".");
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

    @Transactional(readOnly = true)
    public List<Lecture> getLectures() {
        final List<Lecture> lectures = lectureRepository.getLectures();

        return lectures;
    }

    private UserLecture convertToUserLecture(
        final User user,
        final Lecture lecture,
        final boolean isEnrolled
    ) {
        return UserLecture.builder()
            .userId(user.getId())
            .lectureId(lecture.getId())
            .lectureName(lecture.getLectureName())
            .openDate(lecture.getOpenDate())
            .isEnrolled(isEnrolled)
            .build();
    }

}
