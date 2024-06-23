package com.hhp.lecture.domain;

public interface LectureRepository {

    Lecture getLectureByLectureId(final long lectureId);

    void updateLecture(Lecture lecture);

}
