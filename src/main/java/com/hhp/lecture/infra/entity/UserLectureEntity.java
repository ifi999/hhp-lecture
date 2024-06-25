package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Entity
@Table(name = "user_lecture")
public class UserLectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lecture;

    private LocalDateTime createdDate;

    protected UserLectureEntity() {
    }

    public UserLectureEntity(final UserEntity user, final LectureEntity lecture, final LocalDateTime appliedDate) {
        this.user = user;
        this.lecture = lecture;
        this.createdDate = appliedDate;

        if (user != null && !user.getUserLectures().contains(this)) {
            user.getUserLectures().add(this);
        }

        if (lecture != null && !lecture.getUserLectures().contains(this)) {
            lecture.getUserLectures().add(this);
        }
    }

}
