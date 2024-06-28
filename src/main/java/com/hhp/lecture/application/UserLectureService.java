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

        isApplyLectureValid(now, user, lecture);

        lectureRepository.updateLecture(lecture);
        applyHistoryRepository.saveApplyHistory(new ApplyHistory(user.getId(), lecture.getId()));
        userLectureRepository.saveUserLecture(user, lecture, now);

        return convertToUserLecture(user, lecture);
    }

    private void isApplyLectureValid(
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

        if (appliedCount - MAX_PARTICIPANTS > 0) {
            throw new IllegalStateException("The number of participants has already exceeded the maximum allowed limit of " + MAX_PARTICIPANTS + " participants.");
        }
    }

    @Transactional(readOnly = true)
    public List<Lecture> getLectures() {
        final List<Lecture> lectures = lectureRepository.getLectures();

        return lectures;
    }

    private UserLecture convertToUserLecture(
        final User user,
        final Lecture lecture
    ) {
        return UserLecture.builder()
            .userId(user.getId())
            .lectureId(lecture.getId())
            .lectureName(lecture.getLectureName())
            .openDate(lecture.getOpenDate())
            .build();
    }

    @Transactional(readOnly = true)
    public List<UserLecture> getAppliedLectures(final long userId) {
        final User user = userRepository.getUserByUserId(userId);
        final LocalDateTime now = dateTimeProvider.now();

        final List<UserLecture> lectures = userLectureRepository.getAppliedLectures(user, now);

        return lectures;
    }
}
