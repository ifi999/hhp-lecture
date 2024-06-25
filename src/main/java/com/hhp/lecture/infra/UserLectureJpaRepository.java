package com.hhp.lecture.infra;

import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import com.hhp.lecture.infra.entity.UserLectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLectureJpaRepository extends JpaRepository<UserLectureEntity, Long> {

    boolean existsByUserAndLecture(final UserEntity user, final LectureEntity lecture);

}
