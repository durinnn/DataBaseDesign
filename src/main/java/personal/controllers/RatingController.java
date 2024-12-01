package personal.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.DTO.RatingRequest;
import personal.models.Rating;
import personal.services.RatingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // 평점 등록
    @PostMapping
    public ResponseEntity<String> addRating(@RequestBody RatingRequest rating) {
        try {
            System.out.println(rating);
            ratingService.addRating(rating);
            return ResponseEntity.ok("Review added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 평점 수정
    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable Integer id, @RequestParam Integer newRatingValue) {
        try {
            Rating updatedRating = ratingService.updateRating(id, newRatingValue);
            return ResponseEntity.ok(updatedRating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 평점 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer id) {
        try {
            ratingService.deleteRating(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 영화에 대한 모든 평점 조회
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getRatingsByMovieId(@PathVariable Integer movieId) {
        List<Rating> ratings = ratingService.getRatingsByMovieId(movieId);
        return ResponseEntity.ok(ratings);
    }

    // 특정 영화의 평점 통계 조회
    @GetMapping("/movie/{movieId}/statistics")
    public ResponseEntity<Optional<Double>> getRatingStatisticsByMovieId(@PathVariable Integer movieId) {
        Optional<Double> stats = ratingService.getRatingStatisticsByMovieId(movieId);
        return ResponseEntity.ok(stats);
    }

}
