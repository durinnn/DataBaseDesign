package personal.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieSummary {
    private Integer movieId;
    private String title;
    private Long reviewCount;
    private Double avgRating;
    private String genre;
    private String releaseDate;
}
