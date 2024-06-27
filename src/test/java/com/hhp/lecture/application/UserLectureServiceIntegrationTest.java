package com.hhp.lecture.application;

import com.hhp.lecture.domain.DateTimeProvider;
import com.hhp.lecture.domain.UserLecture;
import com.hhp.lecture.infra.LectureJpaRepository;
import com.hhp.lecture.infra.UserJpaRepository;
import com.hhp.lecture.infra.entity.LectureEntity;
import com.hhp.lecture.infra.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private DateTimeProvider dateTimeProvider;

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
        assertThat(신청_후_강의.getId()).isEqualTo(강의_ID);
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

    /**
     * 이미 종료된 강의는 목록에서 제외 검증
     */
    @Test
    void 이미_일정이_지난_신청_완료_강의는_목록에서_제외된다() {
        // given
        final UserEntity 유저 = userRepository.save(new UserEntity("유저"));
        final long 유저_ID = 유저.getId();

        final LectureEntity 지난_강의 = lectureRepository.save(new LectureEntity(
            "4월 토요일 특강",
            LECTURE_APPLY_DATE,
            LECTURE_OPEN_DATE,
            0)
        );
        final long 지난_강의_ID = 지난_강의.getId();

        final LocalDateTime 진행_예정_강의_시간 = dateTimeProvider.now();
        final LectureEntity 진행_예정_강의 = lectureRepository.save(new LectureEntity(
            "진행 예정 토요일 특강",
            진행_예정_강의_시간.minusDays(1L),
            진행_예정_강의_시간.plusDays(3L),
            0)
        );
        final long 진행_예정_강의_ID = 진행_예정_강의.getId();

        final UserLecture 지난_특강신청_요청 = new UserLecture(유저_ID, 지난_강의_ID);
        userLectureService.applyLecture(지난_특강신청_요청);
        final UserLecture 진행예정_특강신청_요청 = new UserLecture(유저_ID, 진행_예정_강의_ID);
        userLectureService.applyLecture(진행예정_특강신청_요청);

        // when
        final List<UserLecture> 신청_완료_목록 = userLectureService.getAppliedLectures(유저_ID);

        // then
        assertThat(신청_완료_목록.size()).isEqualTo(1);
        assertThat(신청_완료_목록.get(0).getLectureName()).isEqualTo("진행 예정 토요일 특강");
    }

    @Test
    void 특강_신청을_30명하면_참가자수는_30이다() throws Exception {
        // given
        final LectureEntity 강의 = lectureRepository.save(new LectureEntity(
            "토요일 특강",
            LECTURE_APPLY_DATE,
            LECTURE_OPEN_DATE,
            0)
        );

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(30);

        for (int i = 0; i < 30; i++) {
            executorService.submit(() -> {
                try {
                    final UserEntity 유저 = userRepository.save(new UserEntity("유저"));
                    return userLectureService.applyLecture(new UserLecture(유저.getId(), 강의.getId()));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(60, TimeUnit.SECONDS);

        // when
        final LectureEntity 신청_완료_강의 = lectureRepository.findById(강의.getId()).get();

        // then
        assertThat(신청_완료_강의.getAppliedCount()).isEqualTo(30);

        executorService.shutdown();
    }

}
