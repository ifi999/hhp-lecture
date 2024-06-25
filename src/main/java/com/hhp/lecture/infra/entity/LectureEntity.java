package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
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

    private LocalDateTime applyDate;

    private LocalDateTime openDate;

    private int appliedCount;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLectureEntity> userLectures = new ArrayList<>();

    protected LectureEntity() {
    }

    public LectureEntity(
        final long id,
        final String lectureName,
        final LocalDateTime applyDate,
        final LocalDateTime openDate,
        final int appliedCount
    ) {
        this.id = id;
        this.lectureName = lectureName;
        this.applyDate = applyDate;
        this.openDate = openDate;
        this.appliedCount = appliedCount;
    }

    public LectureEntity(
        final String lectureName,
        final LocalDateTime applyDate,
        final LocalDateTime openDate,
        final int appliedCount
    ) {
        this.lectureName = lectureName;
        this.applyDate = applyDate;
        this.openDate = openDate;
        this.appliedCount = appliedCount;
    }

}
