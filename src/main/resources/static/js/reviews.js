const REVIEW_API_URL = 'http://localhost:8080/reviews';
// Mock: 로그인 기록에서 가져온 현재 사용자 ID
const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));



// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('user-id').textContent = loggedInUser.id;
    fetchUserReviews(loggedInUser.id);
});

// 리뷰 데이터를 가져오는 함수
async function fetchUserReviews() {
    try {
        // 서버에서 리뷰 데이터 가져오기
        const response = await fetch(`${REVIEW_API_URL}/user/${loggedInUser.id}`, {'method' : 'GET'}); // API 호출
        if (!response.ok) {
            throw new Error('Failed to fetch reviews');
        }

        const reviews = await response.json();
        displayReviews(reviews); // 리뷰 렌더링
    } catch (error) {
        console.error('Error fetching reviews:', error);
        document.getElementById('review-list').innerHTML = `<li class="error">Failed to load reviews</li>`;
    }
}


function editReview(reviewId, title, content) {
    // 기존 리뷰 데이터를 편집 모드로 전환
    const reviewItem = [...document.querySelectorAll('.review-item')].find((item) =>
        item.querySelector('h3').textContent === title
    );

    // 입력 폼 생성 (title은 읽기 전용)
    reviewItem.innerHTML = `
    <h3>${title}</h3> <!-- 제목은 그대로 표시 -->
    <textarea id="edit-content">${content}</textarea> <!-- content만 수정 가능 -->
    <button onclick="saveReview('${reviewId}', '${title}')">Save</button>
    <button onclick="cancelEdit('${reviewId}', '${title}')">Cancel</button>`;
}

function saveReview(reviewId, title) {
    // 서버에 수정 요청 보내기
    // 새로운 content 가져오기
    const reviewItem = [...document.querySelectorAll('.review-item')].find((item) =>
        item.querySelector('h3').textContent === title
    );
    const newContent = reviewItem.querySelector('#edit-content').value;
    fetch(`${REVIEW_API_URL}/${reviewId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, content: newContent }) // title은 그대로, content만 수정
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to update review');
            return response.json();
        })
        .then(() => {
            alert('Review updated successfully!');
            fetchUserReviews(); // 수정된 리뷰 목록 가져오기
        })
        .catch(error => console.error('Error updating review:', error));
}


function deleteReview(reviewId) {
    if (!confirm('Are you sure you want to delete this review?')) return;

    // 서버에 삭제 요청 보내기
    fetch(`${REVIEW_API_URL}/${reviewId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to delete review');
            alert('Review deleted successfully!');
            fetchUserReviews(); // 삭제 후 리뷰 목록 새로고침
        })
        .catch(error => console.error('Error deleting review:', error));
}


function cancelEdit(reviewId, title, content) {
    const reviewItem = [...document.querySelectorAll('.review-item')].find((item) =>
        item.querySelector('h3').textContent === title
    );

    reviewItem.innerHTML = `
    <h3>${title}</h3>
    <p>${content}</p>
    <div class="actions">
        <button onclick="editReview('${reviewId}', '${title}', '${content}')">Edit</button>
        <button onclick="deleteReview('${reviewId}')">Delete</button>
    </div>
`;
}




function displayReviews(reviews) {
    const reviewList = document.getElementById('review-list');
    reviewList.innerHTML = ''; // 기존 리뷰 초기화

    if (reviews.length === 0) {
        reviewList.innerHTML = `<li class="empty">No reviews found for this user.</li>`;
        return;
    }

    reviews.forEach((review) => {
        const [reviewId, title, content] = review; // reviewId, title, content 추출

        const reviewItem = document.createElement('li');
        reviewItem.className = 'review-item';
        reviewItem.innerHTML = `
            <h3>${title}</h3>
            <p>${content}</p>
            <div class="actions">
                <button onclick="editReview('${reviewId}', '${title}', '${content}')">Edit</button>
                <button onclick="deleteReview('${reviewId}')">Delete</button>
            </div>
        `;
        reviewList.appendChild(reviewItem);
    });
}