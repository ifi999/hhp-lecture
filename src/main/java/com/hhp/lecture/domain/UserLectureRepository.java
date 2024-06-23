package com.hhp.lecture.domain;

public interface UserLectureRepository {

    boolean existsByUserAndLecture(final User user, final Lecture lecture);

    UserLecture saveUserLecture(final User user, final Lecture lecture);

}
