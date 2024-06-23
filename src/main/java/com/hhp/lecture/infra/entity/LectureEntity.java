package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
@Table(name = "lecture")
public class LectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String lectureName;

    private int appliedCount;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLectureEntity> userLectures = new ArrayList<>();

    protected LectureEntity() {
    }

    public LectureEntity(final long id, final String lectureName, final int appliedCount) {
        this.id = id;
        this.lectureName = lectureName;
        this.appliedCount = appliedCount;
    }

    public LectureEntity(final String lectureName, final int appliedCount) {
        this.lectureName = lectureName;
        this.appliedCount = appliedCount;
    }

}
