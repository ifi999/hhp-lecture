package com.hhp.lecture.infra;

import com.hhp.lecture.infra.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {
}
