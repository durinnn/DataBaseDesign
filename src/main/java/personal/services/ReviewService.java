package personal.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.DTO.ReviewRequest;
import personal.models.Review;
import personal.repositorys.MovieRepository;
import personal.repositorys.ReviewRepository;
import personal.repositorys.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    
     //리뷰 등록
     //중복 등록 방지
    public void addReview(ReviewRequest review) {
        Review newReview = new Review();
        newReview.setMovie(movieRepository.findById(review.getMovieId()).get());
        newReview.setUsers(userRepository.findById(review.getUsersId()).get());
        newReview.setContent(review.getContent());
        reviewRepository.save(newReview);
    }

    @Transactional
    public Optional<Review> updateReview(Integer reviewId, Review updatedReview) {
        Optional<Review> existingReviewOptional = reviewRepository.findByReviewId(reviewId);

        if (existingReviewOptional.isPresent()) {
            Review existingReview = existingReviewOptional.get();

            existingReview.setContent(updatedReview.getContent());

            Review savedReview = reviewRepository.save(existingReview);
            return Optional.of(savedReview);
        } else {
            throw new IllegalArgumentException("Review not found with ID: " + updatedReview.getReviewId());
        }
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Integer reviewId) {
        Optional<Review> review = reviewRepository.findByReviewId(reviewId);
        if (review.isPresent()) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new IllegalArgumentException("Review not found with ID: " + reviewId);
        }
    }

    //특정 영화에 대한 모든 리뷰 조회 (CTE 활용)
    public List<Object[]> getReviewsByMovieId(Integer movieId) {
        return reviewRepository.findAllByMovieIdWithCTE(movieId);
    }

    //특정 유저의 모든 리뷰 조회
    public List<Object[]> getReviewsByUserId(Integer userId) {
        return reviewRepository.findAllByUserId(userId);
    }
}
