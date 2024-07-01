package com.hhp.lecture.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplyHistoryTest {

    @Test
    void 생성자를_만들_때_유저ID는_양수여야한다() {
        final long 유저ID_0 = 0L;
        final long 유저ID_음수 = -1L;
        final long 강의ID = 1L;

        Assertions.assertThatThrownBy(() -> new ApplyHistory(유저ID_0, 강의ID))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The user's ID must be positive.");

        Assertions.assertThatThrownBy(() -> new ApplyHistory(유저ID_음수, 강의ID))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The user's ID must be positive.");
    }

    @Test
    void 생성자를_만들_때_강의ID는_양수여야한다() {
        final long 유저ID = 1L;
        final long 강의ID_0 = 0L;
        final long 강의ID_음수 = -1L;

        Assertions.assertThatThrownBy(() -> new ApplyHistory(유저ID, 강의ID_0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's ID must be positive.");

        Assertions.assertThatThrownBy(() -> new ApplyHistory(유저ID, 강의ID_음수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The lecture's ID must be positive.");
    }

}
