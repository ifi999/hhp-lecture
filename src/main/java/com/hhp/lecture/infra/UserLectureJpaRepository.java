package com.hhp.lecture.infra;

import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import com.hhp.lecture.infra.entity.UserLectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLectureJpaRepository extends JpaRepository<UserLectureEntity, Long> {

    Optional<UserLectureEntity> findByUserAndLecture(final UserEntity user, final LectureEntity lecture);

}
