package personal.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.DTO.UserResponse;
import personal.models.User;
import personal.services.UserService;

import java.util.List;
import java.util.Optional;
import personal.DTO.LoginRequest;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok("User registered successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 유저 인증 수행
            Optional<User> optionalUser = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // 민감한 정보 제거 후 반환
                UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUsername(), user.getCreatedAt());
                return ResponseEntity.ok(userResponse);
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    // 사용자 ID로 조회
    @GetMapping("/{id}")

    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.searchUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 이름으로 리뷰와 평점 조회
    @GetMapping("/reviews/{name}")

    public ResponseEntity<List<Object[]>> getReviewsByName(@PathVariable String name) {
        List<Object[]> reviews = userService.searchReviewsByName(name);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/ratings/{name}")

    public ResponseEntity<List<Object[]>> getRatingsByName(@PathVariable String name) {
        List<Object[]> ratings = userService.searchRatingsByName(name);
        return ResponseEntity.ok(ratings);
    }

    // 사용자 정보 수정
    @PutMapping("/{id}")

    public ResponseEntity<User> modifyUser(@PathVariable int id, @RequestBody User updatedUser) {
        updatedUser.setId(id); // Ensure the ID matches
        Optional<User> modifiedUser = userService.modifyUser(updatedUser);
        return modifiedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 삭제 (리뷰와 평점도 함께 삭제)
    @DeleteMapping("/{id}")

    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User and associated data deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
