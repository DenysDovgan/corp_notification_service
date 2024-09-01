package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> implements MessageListener {

    public MentorshipAcceptedEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipAcceptedEvent.class, event -> {
            UserDto userDto = userServiceClient.getUser(event.getAuthorId());
            String notificationMessage = getMessage(event, Locale.UK);
            sendNotification(userDto.getId(), notificationMessage);
        });
    }
}
