package com.hhp.lecture.infra;

import com.hhp.lecture.infra.entity.ApplyHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyHistoryJpaRepository extends JpaRepository<ApplyHistoryEntity, Long> {
}
