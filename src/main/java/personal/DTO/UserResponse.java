package personal.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * DTO for {@link personal.models.User}
 */
@Value
public class UserResponse implements Serializable {
    Integer id;
    String name;
    String username;
    String email;
    String createdAt;

    // 생성자
    public UserResponse(Integer id, String name, String email, String username, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt.toString(); // Instant -> String 변환
    }

}