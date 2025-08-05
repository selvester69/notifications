package com.example.userpreferenceservice;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository preferenceRepository;

    // TODO: set default preference as email
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
