package com.hhp.lecture.infra;

import com.hhp.lecture.domain.Lecture;
import com.hhp.lecture.domain.User;
import com.hhp.lecture.domain.UserLecture;
import com.hhp.lecture.domain.UserLectureRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import com.hhp.lecture.infra.entity.UserLectureEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class UserLectureJpaAdapter implements UserLectureRepository {

    private final UserLectureJpaRepository userLectureJpaRepository;

    public UserLectureJpaAdapter(final UserLectureJpaRepository userLectureJpaRepository) {
        this.userLectureJpaRepository = userLectureJpaRepository;
    }

    @Override
    public boolean existsByUserAndLecture(final User user, final Lecture lecture) {
        final UserEntity userEntity = new UserEntity(user.getId(), user.getName());
        final LectureEntity lectureEntity = new LectureEntity(
            lecture.getId(),
            lecture.getLectureName(),
            lecture.getApplyDate(),
            lecture.getOpenDate(),
            lecture.getAppliedCount()
        );

        return userLectureJpaRepository.existsByUserAndLecture(userEntity, lectureEntity);
    }

    @Override
    public UserLecture saveUserLecture(final User user, final Lecture lecture, final LocalDateTime appliedDate) {
        final UserEntity userEntity = new UserEntity(user.getId(), user.getName());
        final LectureEntity lectureEntity = new LectureEntity(
            lecture.getId(),
            lecture.getLectureName(),
            lecture.getApplyDate(),
            lecture.getOpenDate(),
            lecture.getAppliedCount()
        );

        final UserLectureEntity userLectureEntity = userLectureJpaRepository.save(
            new UserLectureEntity(userEntity, lectureEntity, appliedDate)
        );

        return UserLecture.builder()
            .userId(userLectureEntity.getUser().getId())
            .lectureId(userLectureEntity.getLecture().getId())
            .lectureName(userLectureEntity.getLecture().getLectureName())
            .openDate(userLectureEntity.getLecture().getOpenDate())
            .isEnrolled(true)
            .build();
    }

}
