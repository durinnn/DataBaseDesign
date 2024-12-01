package personal.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link personal.models.Review}
 */
@Value
public class ReviewRequest implements Serializable {
    Integer usersId;
    Integer movieId;
    String content;
}