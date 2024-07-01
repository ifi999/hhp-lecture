package com.hhp.lecture.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class LectureTest {

    @Test
    void 생성자를_만들_때_ID는_양수여야한다() {
        final long 강의ID_0 = 0L;
        final long 강의ID_음수 = -1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 신청가능시간 = LocalDateTime.of(2024, 4, 20, 13, 20);
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);
        final int 강의_참가자수 = 0;

        Assertions.assertThatThrownBy(() -> new Lecture(강의ID_0, 강의명, 신청가능시간, 강의시간, 강의_참가자수))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The ID must be positive.");

        Assertions.assertThatThrownBy(() -> new Lecture(강의ID_음수, 강의명, 신청가능시간, 강의시간, 강의_참가자수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The ID must be positive.");
    }

    @Test
    void 생성자를_만들_때_강의명은_있어야만_한다() {
        final long 강의ID = 1L;
        final String 강의명 = null;
        final LocalDateTime 신청가능시간 = LocalDateTime.of(2024, 4, 20, 13, 20);
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);
        final int 강의_참가자수 = 0;

        Assertions.assertThatThrownBy(() -> new Lecture(강의ID, 강의명, 신청가능시간, 강의시간, 강의_참가자수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's name cannot be null.");
    }

    @Test
    void 생성자를_만들_때_신청가능시간은_있어야만_한다() {
        final long 강의ID = 1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 신청가능시간 = null;
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);
        final int 강의_참가자수 = 0;

        Assertions.assertThatThrownBy(() -> new Lecture(강의ID, 강의명, 신청가능시간, 강의시간, 강의_참가자수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's apply date cannot be null.");
    }

    @Test
    void 생성자를_만들_때_강의시간은_있어야만_한다() {
        final long 강의ID = 1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 신청가능시간 = LocalDateTime.of(2024, 4, 20, 13, 20);
        final LocalDateTime 강의시간 = null;
        final int 강의_참가자수 = 0;

        Assertions.assertThatThrownBy(() -> new Lecture(강의ID, 강의명, 신청가능시간, 강의시간, 강의_참가자수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's open date cannot be null.");
    }

    @Test
    void 생성자를_만들_때_참가자수는_양수여야한다() {
        final long 강의ID = 1L;
        final String 강의명 = "토요일 특강";
        final LocalDateTime 신청가능시간 = LocalDateTime.of(2024, 4, 20, 13, 20);
        final LocalDateTime 강의시간 = LocalDateTime.of(2024, 4, 23, 13, 20);
        final int 강의_참가자수_음수 = -1;

        Assertions.assertThatThrownBy(() -> new Lecture(강의ID, 강의명, 신청가능시간, 강의시간, 강의_참가자수_음수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The applied count must be greater than zero.");
    }

}
