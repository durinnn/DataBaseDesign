package personal.DTO;

import lombok.Value;
import personal.models.Movie;
import personal.models.User;

import java.io.Serializable;

/**
 * DTO for {@link personal.models.Rating}
 */
@Value
public class RatingRequest implements Serializable {
    Integer rating;
    Integer usersId;
    Integer movieId;
}