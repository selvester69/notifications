package com.example.userpreferenceservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService preferenceService;

    @PutMapping
    public UserPreference setPreference(@RequestBody UserPreference preference) {
        return preferenceService.setPreference(preference);
    }

    @GetMapping
    public List<UserPreference> getPreferences(@RequestParam String userId, @RequestParam(required = false) String category) {
        if (category != null) {
            return preferenceService.getPreferencesByUserIdAndCategory(userId, category);
        } else {
            return preferenceService.getPreferencesByUserId(userId);
        }
    }

    @PostMapping("/bulk")
    public void bulkSetPreferences(@RequestBody List<UserPreference> preferences) {
        preferenceService.bulkSetPreferences(preferences);
    }
}
