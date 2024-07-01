package com.hhp.lecture.infra;

import com.hhp.lecture.infra.entity.LectureEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {

    @Modifying
    @Query("UPDATE LectureEntity l SET l.appliedCount = l.appliedCount + 1 WHERE l.id = :id")
    void incrementAppliedCount(@Param("id") long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "30000")})
    @Query("SELECT l FROM LectureEntity l WHERE l.id = :id")
    Optional<LectureEntity> findByIdWithLock(@Param("id") long id);

}
