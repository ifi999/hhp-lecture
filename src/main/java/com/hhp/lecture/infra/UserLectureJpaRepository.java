package com.hhp.lecture.infra;

import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import com.hhp.lecture.infra.entity.UserLectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserLectureJpaRepository extends JpaRepository<UserLectureEntity, Long> {

    boolean existsByUserAndLecture(final UserEntity user, final LectureEntity lecture);

    @Query("SELECT ul " +
            " FROM UserLectureEntity ul " +
            "WHERE ul.user = :user " +
            "  AND ul.lecture.openDate >= :now")
    List<UserLectureEntity> findUpcomingLecturesByUser(@Param("user") UserEntity user, @Param("now") LocalDateTime now);

}
