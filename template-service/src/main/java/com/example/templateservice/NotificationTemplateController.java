package com.example.templateservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService templateService;

    @PostMapping
    public NotificationTemplate createTemplate(@RequestBody NotificationTemplate template) {
        return templateService.createTemplate(template);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationTemplate> getTemplateById(@PathVariable UUID id) {
        Optional<NotificationTemplate> template = templateService.getTemplateById(id);
        return template.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<NotificationTemplate> searchTemplate(
            @RequestParam String name,
            @RequestParam ChannelType channel,
            @RequestParam String language) {
        Optional<NotificationTemplate> template = templateService.findTemplate(name, channel, language);
        return template.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationTemplate> updateTemplate(@PathVariable UUID id, @RequestBody NotificationTemplate templateDetails) {
        try {
            NotificationTemplate updatedTemplate = templateService.updateTemplate(id, templateDetails);
            return ResponseEntity.ok(updatedTemplate);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
