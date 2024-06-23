package com.hhp.lecture.infra.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Entity
@Table(name = "apply_history")
public class ApplyHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long lectureId;

    protected ApplyHistoryEntity() {
    }

    @Builder
    public ApplyHistoryEntity(final long userId, final long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }

}
