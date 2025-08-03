package com.example.userpreferenceservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {

    List<UserPreference> findByUserIdAndCategory(String userId, String category);

    List<UserPreference> findByUserId(String userId);
}
