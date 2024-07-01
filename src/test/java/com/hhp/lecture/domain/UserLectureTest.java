package com.hhp.lecture.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class UserLectureTest {

    @Test
    void 생성자를_만들_때_유저ID는_양수여야한다() {
        final long 유저ID_0 = 0L;
        final long 유저ID_음수 = -1L;
        final long 강의ID = 1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);

        Assertions.assertThatThrownBy(() -> new UserLecture(유저ID_0, 강의ID, 강의명, 강의시간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The user's ID must be positive.");

        Assertions.assertThatThrownBy(() -> new UserLecture(유저ID_음수, 강의ID, 강의명, 강의시간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The user's ID must be positive.");
    }

    @Test
    void 생성자를_만들_때_강의ID는_양수여야한다() {
        final long 유저ID = 1L;
        final long 강의ID_0 = 0L;
        final long 강의ID_음수 = -1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);

        Assertions.assertThatThrownBy(() -> new UserLecture(유저ID, 강의ID_0, 강의명, 강의시간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's ID must be positive.");

        Assertions.assertThatThrownBy(() -> new UserLecture(유저ID, 강의ID_음수, 강의명, 강의시간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's ID must be positive.");
    }

    @Test
    void 생성자를_만들_때_강의명은_있어야만_한다() {
        final long 유저ID = 1L;
        final long 강의ID = 1L;
        final String 강의명 = null;
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);

        Assertions.assertThatThrownBy(() -> new UserLecture(유저ID, 강의ID, 강의명, 강의시간))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The lecture's name cannot be null.");
    }

    @Test
    void 생성자를_만들_때_강의시간은_있어야만_한다() {
        final long 유저ID = 1L;
        final long 강의ID = 1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 강의시간 = null;

        Assertions.assertThatThrownBy(() -> new UserLecture(유저ID, 강의ID, 강의명, 강의시간))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's open date cannot be null.");
    }

}
