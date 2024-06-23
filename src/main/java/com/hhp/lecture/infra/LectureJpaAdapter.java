package com.hhp.lecture.infra;

import com.hhp.lecture.domain.Lecture;
import com.hhp.lecture.domain.LectureRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class LectureJpaAdapter implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    public LectureJpaAdapter(final LectureJpaRepository lectureJpaRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
    }

    @Override
    public Lecture getLectureByLectureId(final long lectureId) {
        final LectureEntity lectureEntity = lectureJpaRepository.findById(lectureId)
            .orElseThrow(() -> new EntityNotFoundException("Lecture not found. Lecture ID: " + lectureId));

        return new Lecture(lectureEntity.getId(), lectureEntity.getLectureName(), lectureEntity.getAppliedCount());
    }

    @Override
    public void updateLecture(final Lecture lecture) {
        lecture.incrementAppliedCount();

        final LectureEntity lectureEntity = new LectureEntity(lecture.getId(), lecture.getLectureName(), lecture.getAppliedCount());
        lectureJpaRepository.save(lectureEntity);
    }

}
