package personal.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link personal.models.Review}
 */
@Value
public class ReviewUpdate implements Serializable {
    Integer reviewId;
    String content;
}