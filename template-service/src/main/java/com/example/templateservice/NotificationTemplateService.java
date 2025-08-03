package com.example.templateservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;

    public NotificationTemplate createTemplate(NotificationTemplate template) {
        return templateRepository.save(template);
    }

    public Optional<NotificationTemplate> getTemplateById(UUID id) {
        return templateRepository.findById(id);
    }

    public Optional<NotificationTemplate> findTemplate(String name, ChannelType channel, String language) {
        return templateRepository.findByNameAndChannelAndLanguage(name, channel, language);
    }

    public NotificationTemplate updateTemplate(UUID id, NotificationTemplate templateDetails) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setName(templateDetails.getName());
        template.setChannel(templateDetails.getChannel());
        template.setLanguage(templateDetails.getLanguage());
        template.setSubject(templateDetails.getSubject());
        template.setBody(templateDetails.getBody());
        template.setActive(templateDetails.isActive());

        return templateRepository.save(template);
    }

    public void deleteTemplate(UUID id) {
        templateRepository.deleteById(id);
    }
}
