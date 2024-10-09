package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Long telegramId;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
