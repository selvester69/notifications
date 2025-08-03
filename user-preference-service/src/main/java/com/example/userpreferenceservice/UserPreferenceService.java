package com.example.userpreferenceservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository preferenceRepository;

    public UserPreference setPreference(UserPreference preference) {
        return preferenceRepository.save(preference);
    }

    public List<UserPreference> getPreferencesByUserIdAndCategory(String userId, String category) {
        return preferenceRepository.findByUserIdAndCategory(userId, category);
    }

    public List<UserPreference> getPreferencesByUserId(String userId) {
        return preferenceRepository.findByUserId(userId);
    }

    public void bulkSetPreferences(List<UserPreference> preferences) {
        preferenceRepository.saveAll(preferences);
    }
}
