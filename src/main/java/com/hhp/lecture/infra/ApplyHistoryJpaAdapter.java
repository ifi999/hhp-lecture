package com.hhp.lecture.infra;

import com.hhp.lecture.domain.ApplyHistory;
import com.hhp.lecture.domain.ApplyHistoryRepository;
import com.hhp.lecture.infra.entity.ApplyHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ApplyHistoryJpaAdapter implements ApplyHistoryRepository {

    private final ApplyHistoryJpaRepository applyHistoryJpaRepository;

    public ApplyHistoryJpaAdapter(final ApplyHistoryJpaRepository applyHistoryJpaRepository) {
        this.applyHistoryJpaRepository = applyHistoryJpaRepository;
    }

    @Override
    public ApplyHistory saveApplyHistory(final ApplyHistory applyHistory) {
        final ApplyHistoryEntity applyHistoryEntity = applyHistoryJpaRepository.save(
            ApplyHistoryEntity.builder()
                .userId(applyHistory.getUserId())
                .lectureId(applyHistory.getLectureId())
                .build()
        );

        return ApplyHistory.builder()
            .id(applyHistoryEntity.getId())
            .userId(applyHistoryEntity.getUserId())
            .lectureId(applyHistory.getUserId())
            .build();
    }
}
