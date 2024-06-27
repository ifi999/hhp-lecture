package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "LectureEntity{" +
            "id=" + id +
            ", lectureName='" + lectureName + '\'' +
            ", applyDate=" + applyDate +
            ", openDate=" + openDate +
            ", appliedCount=" + appliedCount +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LectureEntity that = (LectureEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
