package com.hhp.lecture.domain;

import java.time.LocalDateTime;

public interface UserLectureRepository {

    boolean existsByUserAndLecture(final User user, final Lecture lecture);

    UserLecture saveUserLecture(final User user, final Lecture lecture, final LocalDateTime appliedDate);

}
