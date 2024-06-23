package com.hhp.lecture.infra;

import com.hhp.lecture.domain.Lecture;
import com.hhp.lecture.domain.User;
import com.hhp.lecture.domain.UserLecture;
import com.hhp.lecture.domain.UserLectureRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import com.hhp.lecture.infra.entity.UserLectureEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserLectureJpaAdapter implements UserLectureRepository {

    private final UserLectureJpaRepository userLectureJpaRepository;

    public UserLectureJpaAdapter(final UserLectureJpaRepository userLectureJpaRepository) {
        this.userLectureJpaRepository = userLectureJpaRepository;
    }

    @Override
    public boolean existsByUserAndLecture(final User user, final Lecture lecture) {
        final UserEntity userEntity = new UserEntity(user.getId(), user.getName());
        final LectureEntity lectureEntity = new LectureEntity(lecture.getId(), lecture.getLectureName(), lecture.getAppliedCount());

        return userLectureJpaRepository.findByUserAndLecture(userEntity, lectureEntity).isPresent();
    }

    @Override
    public UserLecture saveUserLecture(final User user, final Lecture lecture) {
        final UserEntity userEntity = new UserEntity(user.getId(), user.getName());
        final LectureEntity lectureEntity = new LectureEntity(lecture.getId(), lecture.getLectureName(), lecture.getAppliedCount());

        final UserLectureEntity userLectureEntity = userLectureJpaRepository.save(
            UserLectureEntity.builder()
                .user(userEntity)
                .lecture(lectureEntity)
                .build()
        );

        return UserLecture.builder()
            .userId(userLectureEntity.getUser().getId())
            .lectureId(userLectureEntity.getLecture().getId())
            .lectureName(userLectureEntity.getLecture().getLectureName())
            .isEnrolled(true)
            .build();
    }

}
