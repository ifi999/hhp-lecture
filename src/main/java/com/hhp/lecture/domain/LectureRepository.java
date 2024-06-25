package com.hhp.lecture.domain;

import java.util.List;

public interface LectureRepository {

    Lecture getLectureByLectureId(final long lectureId);

    void updateLecture(Lecture lecture);

    List<Lecture> getLectures();

}
