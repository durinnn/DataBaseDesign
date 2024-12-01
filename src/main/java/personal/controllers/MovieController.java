package personal.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.DTO.MovieRequest;
import personal.DTO.MovieSearch;
import personal.DTO.MovieSummary;
import personal.models.Movie;
import personal.services.MovieService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // 영화 등록
    @PostMapping
    public ResponseEntity<Movie> registerMovie(@RequestBody MovieRequest movie) {
        Movie savedMovie = movieService.registerMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<MovieSummary>> getMovieSummary() {
        List<MovieSummary> movieSummaryList = movieService.getMovieSummaries();
        return ResponseEntity.ok(movieSummaryList);
    }

    // 영화 수정
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie updatedMovie) {
        try {
            Movie updated = movieService.updateMovie(id, updatedMovie);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 영화 삭제 (리뷰와 평점도 함께 삭제)
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMovie(@RequestBody MovieSearch movieRequest) {
        try {
            String title = movieRequest.getTitle();
            LocalDate releaseDate = movieRequest.getReleaseDate();
            movieService.deleteMovie(title, releaseDate);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
        return ResponseEntity.ok("Movie deleted successfully");
    }

    // 영화 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieDetails(@PathVariable Integer id) {
        Optional<Movie> movie = movieService.getMovieDetails(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // 평점 높은 TOP 10 영화  영화 조회
    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieSummary>> getTopRatedMovies() {
        List<MovieSummary> movies = movieService.getTopRatedMovies();
        return ResponseEntity.ok(movies);
    }

    // 특정 장르에 속한 영화 조회
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieSummary>> getMoviesByGenre(@PathVariable String genre) {
        List<MovieSummary> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }

    // 특정 영화 평균 평점 조회
    @GetMapping("/{id}/average-rating")
    public ResponseEntity<Double> getAverageRatingForMovie(@PathVariable Integer id) {
        Double averageRating = movieService.getAverageRatingForMovie(id);
        if (averageRating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(averageRating);
    }

}
