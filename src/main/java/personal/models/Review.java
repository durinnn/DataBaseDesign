package personal.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reviews", schema = "moviereview")public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 리뷰 ID는 고유 ID로 자동 생성
    @Column(name = "review_id", nullable = false)
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY) // 외래 키 설정
    @JoinColumn(name = "user_id", nullable = false) // User 테이블의 기본 키와 매핑
    private User users;

    @ManyToOne(fetch = FetchType.LAZY) // 외래 키 설정
    @JoinColumn(name = "movie_id", nullable = false) // Movie 테이블의 기본 키와 매핑
    private Movie movie;

    @Lob
    @Column(name = "content", nullable = false) // 리뷰 내용
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // 리뷰 생성 시간
    private Instant createdAt;
}
