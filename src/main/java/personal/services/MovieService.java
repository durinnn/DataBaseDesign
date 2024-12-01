package personal.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.DTO.MovieRequest;
import personal.DTO.MovieSummary;
import personal.models.Movie;
import personal.repositorys.MovieRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie registerMovie(MovieRequest movieRequest) {
         // DTO를 Movie 엔티티로 변환
         Movie movie = new Movie();
         movie.setTitle(movieRequest.getTitle());
         movie.setReleaseDate(movieRequest.getReleaseDate());
         movie.setGenre(movieRequest.getGenre());

         // 엔티티 저장
         return movieRepository.save(movie);
     }

     //영화 수정
    public Movie updateMovie(Integer id, Movie updatedMovie) {
        Optional<Movie> existingMovie = movieRepository.findById(id);
        if (existingMovie.isEmpty()) {
            throw new RuntimeException("Movie not found with id: " + id);
        }

        Movie movie = existingMovie.get();
        movie.setTitle(updatedMovie.getTitle());
        movie.setGenre(updatedMovie.getGenre());
        movie.setCreatedAt(updatedMovie.getCreatedAt());
        return movieRepository.save(movie);
    }


    //영화 삭제 (리뷰와 평점도 함께 삭제, 트랜잭션 관리)
    @Transactional
    public void deleteMovie(String title, LocalDate releaseDate) {
        Optional<Movie> movie = movieRepository.findByTitleAndReleaseDate(title, releaseDate);
        movieRepository.delete(movie.get());
    }


    public List<MovieSummary> getMovieSummaries() {
        List<Object[]> rawSummaries = movieRepository.findMovieSummaries(); // Query 반환값: List<Object[]>

        // Convert raw data into MovieSummary DTO
        return rawSummaries.stream()
                .map(row -> new MovieSummary(
                        (Integer) row[0], // movieId
                        (String) row[1],  // title
                        (Long) row[2],    // reviewCount
                        (row[3] != null) ? ((Number) row[3]).doubleValue() : null, // avgRating
                        (String) row[4],  // genre
                        (row[5] != null) ? row[5].toString() : null // releaseDate (formatted as String)
                ))
                .toList();
    }



    //상세 조회
    public Optional<Movie> getMovieDetails(Integer id) {
        return movieRepository.findById(id);
    }



    //특정 영화 평균 평점 조회 (뷰 활용)
    public Double getAverageRatingForMovie(Integer id) {
        return movieRepository.findAverageRatingById(id);
    }

    @Transactional
    //평점 높은 TOP 10 영화 조회 (프로시저 활용)
    public List<MovieSummary> getTopRatedMovies() {
        List<Object[]> rawMovies = movieRepository.getTopRatedMovies(10);
        return rawMovies.stream()
                .map(row -> new MovieSummary(
                        (Integer) row[0], // movieId
                        (String) row[1],  // title
                        (Long) row[2],    // reviewCount
                        (row[3] != null) ? ((Number) row[3]).doubleValue() : null, // avgRating
                        (String) row[4],  // genre
                        (row[5] != null) ? row[5].toString() : null // releaseDate (formatted as String)
                ))
                .toList();
    }

    //특정 장르에 속한 영화 조회
    public List<MovieSummary> getMoviesByGenre(String genre) {
        List<Object[]> rawMovies = movieRepository.findByGenre(genre);

        return rawMovies.stream()
                .map(row -> new MovieSummary(
                        (Integer) row[0], // movieId
                        (String) row[1],  // title
                        (Long) row[2],    // reviewCount
                        (row[3] != null) ? ((Number) row[3]).doubleValue() : null, // avgRating
                        (String) row[4],  // genre
                        (row[5] != null) ? row[5].toString() : null // releaseDate (formatted as String)
                ))
                .toList();
    }


}
