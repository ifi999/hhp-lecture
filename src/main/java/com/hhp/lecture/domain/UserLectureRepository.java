package com.hhp.lecture.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface UserLectureRepository {

    boolean existsByUserAndLecture(User user, Lecture lecture);

    UserLecture saveUserLecture(User user, Lecture lecture, LocalDateTime appliedDate);

    List<UserLecture> getAppliedLectures(final User user, final LocalDateTime now);

}
