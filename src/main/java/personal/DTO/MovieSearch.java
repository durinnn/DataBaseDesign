package personal.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link personal.models.Movie}
 */
@Value
public class MovieSearch implements Serializable {
    String title;
    LocalDate releaseDate;
}