package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Entity
@Table(name = "hhp_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLectureEntity> userLectures = new ArrayList<>();

    protected UserEntity() {
    }

    public UserEntity(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public UserEntity(final String name) {
        this.name = name;
    }

}
