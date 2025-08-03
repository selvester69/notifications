package com.example.orchestratorservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrchestratorService {

    private final WebClient.Builder webClientBuilder;

    public Mono<Void> processEvent(EventData eventData) {
        String category = determineCategory(eventData.getEventType());

        Mono<TemplateResponse> templateMono = webClientBuilder.build()
                .get()
                .uri("http://template-service:8080/templates/search?name={name}&channel={channel}&language={language}",
                        eventData.getEventType(), "EMAIL", "en") // Hardcoded for now
                .retrieve()
                .bodyToMono(TemplateResponse.class);

        Flux<UserPreferenceResponse> preferencesFlux = webClientBuilder.build()
                .get()
                .uri("http://user-preference-service:8080/preferences?userId={userId}&category={category}",
                        eventData.getUserId(), category)
                .retrieve()
                .bodyToFlux(UserPreferenceResponse.class);

        return Mono.zip(templateMono, preferencesFlux.collectList())
                .flatMapMany(tuple -> {
                    TemplateResponse template = tuple.getT1();
                    return Flux.fromIterable(tuple.getT2())
                            .filter(UserPreferenceResponse::isEnabled)
                            .flatMap(preference -> {
                                String renderedSubject = renderTemplate(template.getSubject(), eventData.getData());
                                String renderedBody = renderTemplate(template.getBody(), eventData.getData());
                                NotificationRequest notificationRequest = new NotificationRequest(
                                        eventData.getUserId(),
                                        preference.getChannel(),
                                        new MessageContent(renderedSubject, renderedBody),
                                        Map.of("eventType", eventData.getEventType())
                                );
                                return webClientBuilder.build()
                                        .post()
                                        .uri("http://dispatcher-service:8080/dispatch")
                                        .bodyValue(notificationRequest)
                                        .retrieve()
                                        .bodyToMono(Void.class);
                            });
                }).then();
    }

    private String determineCategory(String eventType) {
        if (eventType.startsWith("ORDER")) {
            return "ORDER";
        } else if (eventType.startsWith("USER")) {
            return "USER_EVENT";
        } else {
            return "MARKETING";
        }
    }

    private String renderTemplate(String template, Map<String, Object> data) {
        // Simple replacement, in a real scenario use a templating engine
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
        }
        return template;
    }
}
