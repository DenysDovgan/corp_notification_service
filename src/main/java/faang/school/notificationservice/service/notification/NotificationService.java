package faang.school.notificationservice.service.notification;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationService {
    void send(UserDto user, String message);
    UserDto.PreferredContact getPreferredContact();
}
