package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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

    protected UserLectureEntity() {
    }

    @Builder
    public UserLectureEntity(final Long id, final UserEntity user, final LectureEntity lecture) {
        this.id = id;
        this.user = user;
        this.lecture = lecture;
    }

    public UserLectureEntity(final UserEntity user, final LectureEntity lecture) {
        this.user = user;
        this.lecture = lecture;

        if (user != null && !user.getUserLectures().contains(this)) {
            user.getUserLectures().add(this);
        }

        if (lecture != null && !lecture.getUserLectures().contains(this)) {
            lecture.getUserLectures().add(this);
        }
    }

}
