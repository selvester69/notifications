package com.example.notificationapigateway;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Preferences", description = "User preference management APIs")
public class UserPreferenceController {

    @Operation(summary = "Get user notification preferences", responses = {
            @ApiResponse(responseCode = "200", description = "User preferences",
                    content = @Content(schema = @Schema(implementation = UserPreferences.class)))
    })
    @GetMapping("/users/{user_id}/preferences")
    public ResponseEntity<UserPreferences> getUserPreferences(@PathVariable("user_id") UUID userId) {
        // Placeholder for actual logic
        UserPreferences prefs = new UserPreferences();
        prefs.setUserId(userId.toString());
        prefs.setPreferences(List.of(
                new UserPreference("SMS", "MARKETING", false, false, null, null),
                new UserPreference("EMAIL", "TRANSACTIONAL", true, false, null, null)
        ));
        return ResponseEntity.ok(prefs);
    }

    @Operation(summary = "Update user notification preferences", responses = {
            @ApiResponse(responseCode = "200", description = "Preferences updated",
                    content = @Content(schema = @Schema(implementation = UserPreferences.class)))
    })
    @PutMapping("/users/{user_id}/preferences")
    public ResponseEntity<UserPreferences> updateUserPreferences(@PathVariable("user_id") UUID userId, @RequestBody UpdateUserPreferencesRequest request) {
        // Placeholder for actual logic
        UserPreferences prefs = new UserPreferences();
        prefs.setUserId(userId.toString());
        prefs.setPreferences(request.getPreferences());
        return ResponseEntity.ok(prefs);
    }
}

@Data
class UserPreferences {
    private String userId;
    private List<UserPreference> preferences;
}

@Data
class UserPreference {
    private String channel;
    private String category;
    private boolean is_enabled;
    private boolean do_not_disturb;
    private LocalTime preferred_time_start;
    private LocalTime preferred_time_end;

    public UserPreference(String channel, String category, boolean is_enabled, boolean do_not_disturb, LocalTime preferred_time_start, LocalTime preferred_time_end) {
        this.channel = channel;
        this.category = category;
        this.is_enabled = is_enabled;
        this.do_not_disturb = do_not_disturb;
        this.preferred_time_start = preferred_time_start;
        this.preferred_time_end = preferred_time_end;
    }
}

@Data
class UpdateUserPreferencesRequest {
    private List<UserPreference> preferences;
}
