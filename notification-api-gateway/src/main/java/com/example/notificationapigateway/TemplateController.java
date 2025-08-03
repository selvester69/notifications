package com.example.notificationapigateway;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Templates", description = "Template management APIs")
public class TemplateController {

    @Operation(summary = "Create a new template", responses = {
            @ApiResponse(responseCode = "201", description = "Template created",
                    content = @Content(schema = @Schema(implementation = TemplateResponse.class)))
    })
    @PostMapping("/templates")
    public ResponseEntity<TemplateResponse> createTemplate(@RequestBody CreateTemplateRequest request) {
        // Placeholder for actual logic
        return new ResponseEntity<>(new TemplateResponse(
                UUID.randomUUID(), request.getName(), request.getChannel(), request.getLanguage(),
                request.getSubject(), request.getContent(), LocalDateTime.now(), LocalDateTime.now()
        ), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a template", responses = {
            @ApiResponse(responseCode = "200", description = "Template data",
                    content = @Content(schema = @Schema(implementation = TemplateResponse.class)))
    })
    @GetMapping("/templates/{template_id}")
    public ResponseEntity<TemplateResponse> getTemplate(@PathVariable("template_id") UUID templateId) {
        // Placeholder for actual logic
        return ResponseEntity.ok(new TemplateResponse(
                templateId, "ORDER_CONFIRMATION", "EMAIL", "en",
                "Your order {{order_id}} has been confirmed!",
                "Hi {{user_name}}, your order {{order_id}} will be delivered by {{delivery_date}}.",
                LocalDateTime.now(), LocalDateTime.now()
        ));
    }
}

@Data
class CreateTemplateRequest {
    private String name;
    private String channel;
    private String language;
    private String subject;
    private String content;
}

@Data
class TemplateResponse {
    private UUID template_id;
    private String name;
    private String channel;
    private String language;
    private String subject;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public TemplateResponse(UUID template_id, String name, String channel, String language, String subject, String content, LocalDateTime created_at, LocalDateTime updated_at) {
        this.template_id = template_id;
        this.name = name;
        this.channel = channel;
        this.language = language;
        this.subject = subject;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
