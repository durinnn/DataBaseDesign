package personal.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.DTO.RatingRequest;
import personal.models.Movie;
import personal.models.Rating;
import personal.models.User;
import personal.repositorys.MovieRepository;
import personal.repositorys.RatingRepository;
import personal.repositorys.ReviewRepository;
import personal.repositorys.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;


    //평점 등록
    @Transactional
    public void addRating(RatingRequest rating) {
        Optional<Movie> movie = movieRepository.findById(rating.getMovieId());
        Optional<User> user = userRepository.findById(rating.getUsersId());

        Rating newRating = new Rating();
        newRating.setMovie(movie.get());
        newRating.setUser(user.get());
        newRating.setRating(rating.getRating());
        ratingRepository.save(newRating);
    }

    //평점 수정
    @Transactional
    public Rating updateRating(Integer id, Integer newRatingValue) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with ID: " + id));
        rating.setRating(newRatingValue);
        return ratingRepository.save(rating);
    }

    //평점 삭제
    @Transactional
    public void deleteRating(Integer id) {
        if (!ratingRepository.existsById(id)) {
            throw new IllegalArgumentException("Rating not found with ID: " + id);
        }
        ratingRepository.deleteById(id);
    }

    //영화에 대한 모든 평점 조회
    public List<Rating> getRatingsByMovieId(Integer movieId) {
        return ratingRepository.findAllByMovieId(movieId);
    }

    //특정 영화의 평점 통계 조회 (뷰 활용)
    public Optional<Double> getRatingStatisticsByMovieId(Integer movieId) {
        return ratingRepository.getRatingStatisticsByMovieId(movieId);
    }

}
