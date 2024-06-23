package com.hhp.lecture.infra;

import com.hhp.lecture.domain.User;
import com.hhp.lecture.domain.UserRepository;
import com.hhp.lecture.infra.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserJpaAdapter(final UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User getUserByUserId(final long userId) {
        final UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found. User ID: " + userId));

        return User.builder()
            .id(userEntity.getId())
            .name(userEntity.getName())
            .build();
    }

}
