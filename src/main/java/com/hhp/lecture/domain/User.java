package com.hhp.lecture.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@ToString
public class User {

    private long id;
    private String name;

    @Builder
    public User(final long id, final String name) {
        Assert.isTrue(id > 0, "The ID must be positive.");
        Assert.notNull(name, "The name cannot be null.");

        this.id = id;
        this.name = name;
    }

}
