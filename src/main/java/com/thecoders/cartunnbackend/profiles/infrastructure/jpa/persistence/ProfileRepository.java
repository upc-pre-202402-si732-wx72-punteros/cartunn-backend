package com.thecoders.cartunnbackend.profiles.infrastructure.jpa.persistence;

import com.thecoders.cartunnbackend.profiles.domain.model.aggregates.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByEmail(String title);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdIsNot(String email, Long id);
}
