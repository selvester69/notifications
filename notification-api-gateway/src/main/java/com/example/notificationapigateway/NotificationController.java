package com.example.notificationapigateway;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {

    @Operation(summary = "Send a notification to a user", responses = {
            @ApiResponse(responseCode = "200", description = "Notification accepted",
                    content = @Content(schema = @Schema(implementation = SendNotificationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping("/notifications/send")
    public ResponseEntity<SendNotificationResponse> sendNotification(@RequestBody SendNotificationRequest request) {
        // Placeholder for actual logic
        return ResponseEntity.ok(new SendNotificationResponse(UUID.randomUUID(), "SCHEDULED"));
    }

    @Operation(summary = "Get notification status", responses = {
            @ApiResponse(responseCode = "200", description = "Notification status",
                    content = @Content(schema = @Schema(implementation = NotificationStatus.class)))
    })
    @GetMapping("/notifications/{notification_id}")
    public ResponseEntity<NotificationStatus> getNotificationStatus(@PathVariable("notification_id") UUID notificationId) {
        // Placeholder for actual logic
        return ResponseEntity.ok(new NotificationStatus(notificationId, "SENT", "EMAIL", LocalDateTime.now(), null, null));
    }

    @Operation(summary = "Bulk/Broadcast Notification", responses = {
            @ApiResponse(responseCode = "200", description = "Broadcast accepted",
                    content = @Content(schema = @Schema(implementation = BroadcastNotificationResponse.class)))
    })
    @PostMapping("/notifications/broadcast")
    public ResponseEntity<BroadcastNotificationResponse> broadcastNotification(@RequestBody BroadcastNotificationRequest request) {
        // Placeholder for actual logic
        return ResponseEntity.ok(new BroadcastNotificationResponse(UUID.randomUUID(), 100000));
    }
}

@Data
class SendNotificationRequest {
    private UUID user_id;
    private String template_name;
    private List<String> channel;
    private String language;
    private LocalDateTime schedule_at;
    private Map<String, String> data;
}

@Data
class SendNotificationResponse {
    private UUID notification_id;
    private String status;

    public SendNotificationResponse(UUID notification_id, String status) {
        this.notification_id = notification_id;
        this.status = status;
    }
}

@Data
class NotificationStatus {
    private UUID notification_id;
    private String status;
    private String channel;
    private LocalDateTime sent_at;
    private LocalDateTime read_at;
    private String error_log;

    public NotificationStatus(UUID notification_id, String status, String channel, LocalDateTime sent_at, LocalDateTime read_at, String error_log) {
        this.notification_id = notification_id;
        this.status = status;
        this.channel = channel;
        this.sent_at = sent_at;
        this.read_at = read_at;
        this.error_log = error_log;
    }
}

@Data
class BroadcastNotificationRequest {
    private String template_name;
    private List<String> channel;
    private String language;
    private Map<String, String> filter;
    private Map<String, String> data;
}

@Data
class BroadcastNotificationResponse {
    private UUID broadcast_id;
    private Integer total_scheduled;

    public BroadcastNotificationResponse(UUID broadcast_id, Integer total_scheduled) {
        this.broadcast_id = broadcast_id;
        this.total_scheduled = total_scheduled;
    }
}
