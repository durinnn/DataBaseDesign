package personal.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import personal.models.Movie;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query(value = "SELECT m.movie_id AS movieId, m.title AS title, COUNT(r.rating_id) AS reviewCount, AVG(r.rating) AS avgRating, m.genre AS genre, m.release_date AS releaseDate " +
            "FROM movieratingssummary m LEFT JOIN ratings r ON m.movie_id = r.movie_id " +
            "GROUP BY m.movie_id, m.title", nativeQuery = true)
    List<Object[]> findMovieSummaries();

    // 특정 장르의 영화 조회
    @Query(value = "SELECT m.movie_id AS movieId, m.title AS title, COUNT(r.rating_id) AS reviewCount, AVG(r.rating) AS avgRating, m.genre AS genre, m.release_date AS releaseDate " +
            "FROM movieratingssummary m LEFT JOIN ratings r ON m.movie_id = r.movie_id WHERE m.genre = :genre " +
            "GROUP BY m.movie_id, m.title", nativeQuery = true)
    List<Object[]> findByGenre(@Param("genre") String genre);
    Optional<Movie> findByTitleAndReleaseDate(String title, LocalDate releaseDate);

    // 특정 영화의 평균 평점 조회
    // 프로시저 호출
    @Procedure(name = "GetAverageRating")
    Double findAverageRatingById(@Param("movieId") Integer movieId);

    // 평점 높은 TOP 영화 조회 (프로시저 활용)
    @Procedure(procedureName = "GetTopRatedMovies")
    List<Object[]> getTopRatedMovies(@Param("limitCount") Integer limitCount);
}
