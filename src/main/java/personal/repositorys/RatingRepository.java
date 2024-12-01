package personal.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.DTO.RatingRequest;
import personal.models.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    // 특정 평점 ID로 평점 조회
    Optional<Rating> findById(Integer id);

    // 특정 평점 ID로 평점 삭제
    void deleteById(Integer id);

    // 특정 영화에 대한 모든 평점 조회
    @Query("SELECT r FROM Rating r WHERE r.movie.id = :movieId")
    List<Rating> findAllByMovieId(Integer movieId);


    // 영화 평점 통계 조회 (뷰 활용)
    @Query(value = "SELECT avg_rating FROM MovieRatingStats WHERE movie_id = :movieId", nativeQuery = true)
    Optional<Double> getRatingStatisticsByMovieId(Integer movieId);

    //특정 사용자가 남긴 평점 조회 (뷰 활용)
    @Query(value = "SELECT * FROM UserReviewRatings WHERE user_name = :name", nativeQuery = true)
    List<Object[]> findReviewsAndRatingsByName(String name);
}
