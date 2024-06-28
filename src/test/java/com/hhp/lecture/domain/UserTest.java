package com.hhp.lecture.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void 생성자를_만들_때_유저ID는_양수여야한다() {
        final long 유저ID_0 = 0L;
        final long 유저ID_음수 = -1L;
        final String 이름 = "유저";

        Assertions.assertThatThrownBy(() -> new User(유저ID_0, 이름))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The ID must be positive.");

        Assertions.assertThatThrownBy(() -> new User(유저ID_음수, 이름))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The ID must be positive.");
    }

    @Test
    void 생성자를_만들_때_이름은은_있어야만_한다() {
        final long 유저ID = 1L;
        final String 이름 = null;

        Assertions.assertThatThrownBy(() -> new User(유저ID, 이름))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("The name cannot be null.");
    }

}
