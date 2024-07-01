package com.hhp.lecture.infra;

import com.hhp.lecture.domain.Lecture;
import com.hhp.lecture.domain.LectureRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

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

        return new Lecture(
            lectureEntity.getId(),
            lectureEntity.getLectureName(),
            lectureEntity.getApplyDate(),
            lectureEntity.getOpenDate(),
            lectureEntity.getAppliedCount()
        );
    }

    @Override
    public void updateLecture(final Lecture lecture) {
        final long lectureId = lecture.getId();
        final LectureEntity lectureEntity = lectureJpaRepository.findByIdWithLock(lectureId)
            .orElseThrow(() -> new EntityNotFoundException("Lecture not found. Lecture ID: " + lectureId));

        lectureJpaRepository.incrementAppliedCount(lectureEntity.getId());
    }

    @Override
    public List<Lecture> getLectures() {
        final List<LectureEntity> lectureEntities = lectureJpaRepository.findAll();

        return lectureEntities.stream()
            .map(o ->
                new Lecture(
                    o.getId(),
                    o.getLectureName(),
                    o.getApplyDate(),
                    o.getOpenDate(),
                    o.getAppliedCount()
                )
            )
            .toList();
    }

}
