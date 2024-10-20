package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AchievementEventListener extends AbstractKafkaEventListener<AchievementEvent, String> {
    public AchievementEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<AchievementEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }


    @Override
    @KafkaListener(topics = "${spring.kafka.topics.achievement-received.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(String message) {
        AchievementEvent event = null;
        try {
            event = objectMapper.readValue(message, AchievementEvent.class);
        } catch (IOException e) {
            log.error("error converting json to obj", e);
        }
        log.info("received: {}", event);
        UserDto user = userServiceClient.getUser(event.getUserId());
        try {
            sendNotification(user, getMessage(user, event));
        } catch (Exception e) {
            log.error("notification didnt send", e);
            return;
        }
        log.info("notification sent: {} ", event);
    }
}
