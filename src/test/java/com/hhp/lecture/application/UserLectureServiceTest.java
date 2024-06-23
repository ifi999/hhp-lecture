package com.hhp.lecture.application;

import com.hhp.lecture.domain.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserLectureServiceTest {

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplyHistoryRepository applyHistoryRepository;

    @Mock
    private UserLectureRepository userLectureRepository;

    @InjectMocks
    private UserLectureService userLectureService;

    /**
     * 특강 신청 성공 테스트
     */
    @Test
    void 특강신청을_한다() {
        // given
        final long 유저_ID = 123L;
        final long 강의_ID = 456L;
        final LocalDateTime 신청시간 = LocalDateTime.of(2024, 4, 20, 13, 30);
        final UserLecture 특강신청_요청 = new UserLecture(유저_ID, 강의_ID);

        given(userRepository.getUserByUserId(유저_ID))
            .willReturn(new User(123L, "유저"));
        given(lectureRepository.getLectureByLectureId(강의_ID))
            .willReturn(new Lecture(456L, "토요일 특강", 0));

        // when
        UserLecture 유저_강의 = userLectureService.applyLecture(특강신청_요청, 신청시간);

        // then
        assertThat(유저_강의.getUserId()).isEqualTo(123L);
        assertThat(유저_강의.getLectureId()).isEqualTo(456L);
        assertThat(유저_강의.getLectureName()).isEqualTo("토요일 특강");
        assertThat(유저_강의.isEnrolled()).isTrue();
    }

    /**
     * 특강 신청자가 30명을 초과 시 요청 실패 테스트
     */
    @Test
    void 특강_신청자가_30명이_채워졌다면_신청을_할_수_없다() {
        // given
        final long 유저_ID = 123L;
        final long 강의_ID = 456L;
        final LocalDateTime 신청시간 = LocalDateTime.of(2024, 4, 20, 13, 30);
        final UserLecture 특강신청_요청 = new UserLecture(유저_ID, 강의_ID);

        given(userRepository.getUserByUserId(유저_ID))
            .willReturn(new User(123L, "유저"));
        given(lectureRepository.getLectureByLectureId(강의_ID))
            .willReturn(new Lecture(456L, "토요일 특강", 30));

        // when
        UserLecture 유저_강의 = userLectureService.applyLecture(특강신청_요청, 신청시간);

        // then
        assertThat(유저_강의.getUserId()).isEqualTo(123L);
        assertThat(유저_강의.getLectureId()).isEqualTo(456L);
        assertThat(유저_강의.getLectureName()).isEqualTo("토요일 특강");
        assertThat(유저_강의.isEnrolled()).isFalse();
    }

    /**
     * 특강 신청 시 강의 신청 내역 기록 테스트
     */
    @Test
    void 특강_신청을_하면_내역이_남는다() {
        // given
        final long 유저_ID = 123L;
        final long 강의_ID = 456L;
        final LocalDateTime 신청시간 = LocalDateTime.of(2024, 4, 20, 13, 30);
        final UserLecture 특강신청_요청 = new UserLecture(유저_ID, 강의_ID);

        given(userRepository.getUserByUserId(유저_ID))
            .willReturn(new User(123L, "유저"));
        given(lectureRepository.getLectureByLectureId(강의_ID))
            .willReturn(new Lecture(456L, "토요일 특강", 0));

        // when
        userLectureService.applyLecture(특강신청_요청, 신청시간);

        // then
        verify(applyHistoryRepository, times(1))
            .saveApplyHistory(argThat(applyHistory ->
                applyHistory.getUserId() == 123L &&
                applyHistory.getLectureId() == 456L
        ));
    }

    /**
     * 특강 신청 시 지정 시간(4월 20일 오후 1시) 검증 테스트
     */
    @Test
    void 특강_신청_요청이_4월_20일_오후1시_이전이면_예외_발생() {
        // given
        final long 유저_ID = 123L;
        final long 강의_ID = 456L;
        final LocalDateTime 신청시간 = LocalDateTime.of(2024, 4, 20, 13, 00);
        final UserLecture 특강신청_요청 = new UserLecture(유저_ID, 강의_ID);

        given(userRepository.getUserByUserId(유저_ID))
            .willReturn(new User(123L, "유저"));
        given(lectureRepository.getLectureByLectureId(강의_ID))
            .willReturn(new Lecture(456L, "토요일 특강", 0));

        // when

        // then
        assertThatThrownBy(() -> userLectureService.applyLecture(특강신청_요청, 신청시간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid application date: 2024-04-20T13:00. Application date must be before 2024-04-20T13:20.");
    }

}