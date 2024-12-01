package personal.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.DTO.ReviewRequest;
import personal.DTO.ReviewUpdate;
import personal.models.Review;
import personal.services.ReviewService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<String> addReview(@RequestBody ReviewRequest review) {
        try {
            reviewService.addReview(review);
            return ResponseEntity.ok("Review added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewUpdate> updateReview(@PathVariable Integer reviewId, @RequestBody Review updatedReview) {
        try {

            Optional<Review> newreview = reviewService.updateReview(reviewId, updatedReview);
            System.out.println("컨트롤러" + newreview.get().getReviewId());
            System.out.println("컨트롤러" + newreview.get().getContent());
            ReviewUpdate response = new ReviewUpdate(newreview.get().getReviewId(), newreview.get().getContent());

            return ResponseEntity.ok(response); // 업데이트된 리뷰 반환
        } catch (EntityNotFoundException e) {
            // 리뷰를 찾을 수 없는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("Review deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 영화에 대한 모든 리뷰 조회
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Object[]>> getReviewsByMovieId(@PathVariable Integer movieId) {
        List<Object[]> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    // 특정 유저의 모든 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Object[]>> getReviewsByUserId(@PathVariable Integer userId) {
        List<Object[]> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}
