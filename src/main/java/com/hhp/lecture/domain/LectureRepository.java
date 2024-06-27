package com.hhp.lecture.domain;

import java.util.List;

public interface LectureRepository {

    Lecture getLectureByLectureId(long lectureId);

    void updateLecture(Lecture lecture);

    List<Lecture> getLectures();

}
