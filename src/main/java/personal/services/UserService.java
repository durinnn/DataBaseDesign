package personal.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.models.Review;
import personal.models.Rating;
import personal.models.User;
import personal.repositorys.UserRepository;
import personal.repositorys.ReviewRepository;
import personal.repositorys.RatingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;

    // 회원가입
    public void saveUser(User users) {

        userRepository.save(users);
    }

    // 사용자 인증 로직
    public Optional<User> authenticate(String username, String password) {
        // 사용자 이름으로 조회
        Optional<User> userOptional = userRepository.findByUsername(username);

        // 사용자가 존재하고, 비밀번호가 일치하는 경우 사용자 반환
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return userOptional; // 인증 성공 시 사용자 정보 반환
            }
        }

        return Optional.empty(); // 인증 실패 시 빈 Optional 반환
    }


    // 조회 (사용자 ID로 조회)
    public Optional<User> searchUserById(int id) {
        return userRepository.findById(id);
    }

    // 조회 (사용자 이름으로 리뷰와 평점 조회)
    public List<Object[]> searchReviewsByName(String name) {
        return reviewRepository.findReviewsAndRatingsByName(name); // 사용자 이름으로 리뷰 조회
    }

    public List<Object[]> searchRatingsByName(String name) {
        return ratingRepository.findReviewsAndRatingsByName(name); // 사용자 이름으로 평점 조회
    }

    // 수정
    public Optional<User> modifyUser(User newUser) {
        return userRepository.findById(newUser.getId())
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                });
    }

    // 삭제 (리뷰와 평점도 함께 삭제, 트랜잭션 처리)
    @Transactional
    public void deleteUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            // 사용자 리뷰 삭제
            reviewRepository.deleteById(id);
            // 사용자 평점 삭제
            ratingRepository.deleteById(id);
            // 사용자 삭제
            userRepository.delete(user.get());
        } else {
            System.out.println("User not found in DB");
        }
    }
}
