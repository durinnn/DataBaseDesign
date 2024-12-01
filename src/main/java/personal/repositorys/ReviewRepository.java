package personal.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.models.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {


    // 특정 리뷰 ID로 리뷰 조회
    Optional<Review> findByReviewId(Integer reviewId);

    // 특정 리뷰 ID로 리뷰 삭제
    void deleteById(int reviewId);

    // 영화에 대한 모든 리뷰 조회 (CTE 사용)
    @Query(value = "WITH MovieReviews AS ( SELECT rating, content FROM reviewseachmovies WHERE movie_id = :movieId)  SELECT * FROM MovieReviews ", nativeQuery = true)
    List<Object[]> findAllByMovieIdWithCTE(Integer movieId);

    // 특정 사용자(유저)의 모든 리뷰 조회
    @Query(value = "WITH UserReviews AS(SELECT ReviewId, MovieTitle, ReviewContent FROM user_movie_reviews WHERE userId = :userId) SELECT * FROM UserReviews", nativeQuery = true)
    List<Object[]> findAllByUserId(Integer userId);

    //특정 유저가 작성한 영화 리뷰들 확인
    @Query(value = "SELECT * FROM UserReviewRatings WHERE user_name = :name", nativeQuery = true)
    List<Object[]> findReviewsAndRatingsByName(String name);
}
