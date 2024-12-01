package personal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "movies", schema = "moviereview")
public class Movie {
    @Id
    @Column(name = "movie_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 설정

    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "genre", nullable = false, length = 50)
    private String genre;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}