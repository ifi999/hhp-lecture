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
        final Lecture lecture = lectureRepository.getLectureByLectureIdWithLock(userLecture.getLectureId());
        validateAlreadyApplied(user, lecture);

        final LocalDateTime now = dateTimeProvider.now();
        lecture.validateLectureApplication(now);

        lectureRepository.updateLecture(lecture);
        applyHistoryRepository.saveApplyHistory(new ApplyHistory(user.getId(), lecture.getId()));
        userLectureRepository.saveUserLecture(user, lecture, now);

        return convertToUserLecture(user, lecture);
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

    private void validateAlreadyApplied(final User user, final Lecture lecture) {
        final boolean isApplied = userLectureRepository.existsByUserAndLecture(user, lecture);

        if (isApplied) {
            throw new IllegalArgumentException("Already applied lecture. Lecture ID: " + lecture.getId() + ".");
        }
    }

}
