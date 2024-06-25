package com.hhp.lecture.application;

import com.hhp.lecture.domain.UserLecture;
import com.hhp.lecture.infra.LectureJpaRepository;
import com.hhp.lecture.infra.UserJpaRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserLectureServiceIntegrationTest {

    @Autowired
    private UserLectureService userLectureService;

    @Autowired
    private LectureJpaRepository lectureRepository;

    @Autowired
    private UserJpaRepository userRepository;

    private static final LocalDateTime LECTURE_APPLY_DATE = LocalDateTime.of(2024, 4, 20, 13, 20);
    private static final LocalDateTime LECTURE_OPEN_DATE = LocalDateTime.of(2024, 4, 23, 13, 20);

    /**
     * 강의 신청 시 참가자 수 증가 검증 테스트
     */
    @Test
    void 특강_신청에_성공하면_특강_참가자수가_늘어난다() {
        // given
        final UserEntity 유저 = userRepository.save(new UserEntity("유저"));
        final LectureEntity 강의 = lectureRepository.save(new LectureEntity(
            "토요일 특강",
            LECTURE_APPLY_DATE,
            LECTURE_OPEN_DATE,
            0)
        );

        final long 유저_ID = 유저.getId();
        final long 강의_ID = 강의.getId();
        final UserLecture 특강신청_요청 = new UserLecture(유저_ID, 강의_ID);

        // when
        userLectureService.applyLecture(특강신청_요청);

        // then
        final LectureEntity 신청_후_강의 = lectureRepository.findById(강의_ID).orElseThrow();
        assertThat(신청_후_강의.getId()).isEqualTo(유저_ID);
        assertThat(신청_후_강의.getLectureName()).isEqualTo("토요일 특강");
        assertThat(신청_후_강의.getApplyDate()).isEqualTo("2024-04-20T13:20:00");
        assertThat(신청_후_강의.getOpenDate()).isEqualTo("2024-04-23T13:20:00");
        assertThat(신청_후_강의.getAppliedCount()).isEqualTo(1);
    }

    /**
     * 강의 중복 신청 방지 테스트
     */
    @Test
    void 이미_특강에_신청한_사람은_다시_신청할_수_없다() {
        // given
        final UserEntity 유저 = userRepository.save(new UserEntity("유저"));
        final LectureEntity 강의 = lectureRepository.save(new LectureEntity(
            "토요일 특강",
            LECTURE_APPLY_DATE,
            LECTURE_OPEN_DATE,
            0)
        );

        final long 유저_ID = 유저.getId();
        final long 강의_ID = 강의.getId();
        final UserLecture 특강신청_요청 = new UserLecture(유저_ID, 강의_ID);
        userLectureService.applyLecture(특강신청_요청);

        final UserLecture 특강신청_중복요청 = new UserLecture(유저_ID, 강의_ID);

        // when

        // then
        assertThatThrownBy(() -> userLectureService.applyLecture(특강신청_중복요청))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Already applied lecture. User Id: " + 유저_ID + ", Lecture ID: " + 강의_ID + ".");
    }

}
