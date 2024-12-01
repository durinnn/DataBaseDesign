const API_URL = 'http://localhost:8080/';
// Fetch logged-in user info from localStorage
const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));

// Parse the movieId from the query string
const urlParams = new URLSearchParams(window.location.search);
const movieId = urlParams.get('movieId');

// Fetch movie details and display them
async function fetchMovieDetails() {
    if (!movieId) {
        alert('Movie ID is missing in the URL.');
        return;
    }

    try {
        const response = await fetch(`${API_URL}movies/${movieId}`,{method: 'GET'});
        if (!response.ok) {
            throw new Error('Failed to fetch movie details.');
        }

        const movie = await response.json();
        document.getElementById('movie-info').innerHTML = `
      <h2>${movie.title}</h2>
      <p>Genre: ${movie.genre}</p>
      <p>Release Date: ${new Date(movie.releaseDate).toLocaleDateString()}</p>
    `;
    } catch (error) {
        console.error('Error fetching movie details:', error);
        alert('Failed to load movie details.');
    }
}

// Load movie details on page load
document.addEventListener('DOMContentLoaded', fetchMovieDetails, fetchAndRenderReviews);

async function fetchAndRenderReviews() {
    if (!movieId) {
        alert('Movie ID is missing in the URL.');
        return;
    }

    try {
        // 영화 평균 평점 데이터 가져오기
        const response = await fetch(`${API_URL}ratings/movie/${movieId}/statistics`, {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error('Failed to fetch rating.');
        }
        // 응답 본문에서 실제 데이터를 추출
        const rating = await response.text(); // 서버에서 단순 텍스트를 반환한다고 가정

        // 평점 출력
        document.getElementById('rating-display').innerHTML = `<strong>평점 ${rating}</strong><br>`;

    } catch (error) {
        console.error('Error fetching rating:', error);
        alert('Failed to load rating.');
    }

    try {
        // 영화 리뷰 데이터 가져오기
        const response = await fetch(`${API_URL}reviews/movie/${movieId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error('Failed to fetch reviews.');
        }

        const reviews = await response.json(); // JSON 데이터 파싱

        reviewList.innerHTML = ''; // 기존 내용 초기화

        if (reviews.length === 0) {
            reviewList.innerHTML = '<li>No reviews available for this movie.</li>';
            return;
        }

// 리뷰 목록 렌더링
        reviews.forEach((review) => {
            // 배열에서 rating과 content 추출
            const [rating, content] = review;

            const listItem = document.createElement('li');
            listItem.innerHTML = `
        <span>Rating: ${rating}/10</span><br>
        <p>${content}</p>
        <hr>
    `;
            reviewList.appendChild(listItem);
        });
    } catch (error) {
        console.error('Error fetching reviews:', error);
        alert('Failed to load reviews.');
    }
}

// DOMContentLoaded 이벤트에 fetchAndRenderReviews 연결
document.addEventListener('DOMContentLoaded', fetchAndRenderReviews);



// Add event listener for review submission
document.getElementById('review-form').addEventListener('submit', async (event) => {
    event.preventDefault(); // Prevent default form submission behavior

    // Get input values
    const reviewContent = document.getElementById('review-content').value;
    const rating = parseFloat(document.getElementById('rating').value);
    const movieId = new URLSearchParams(window.location.search).get('movieId'); // Get movieId from query parameter

    // Validate rating input
    if (rating < 1 || rating > 10 || rating % 0.5 !== 0) {
        alert('Rating must be between 1 and 10, in 0.5 increments.');
        return;
    }

    // Prepare review and rating objects
    const reviewData = {
        content: reviewContent,
        movieId: parseInt(movieId, 10),
        usersId: loggedInUser.id
    };

    const ratingData = {
        rating: rating,
        movieId: parseInt(movieId, 10),
        usersId: loggedInUser.id
    };

    try {
        // Send review to /reviews endpoint
        const reviewResponse = await fetch(`${API_URL}reviews`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(reviewData),
        });

        if (!reviewResponse.ok) {
            throw new Error('Failed to add review. ' + (await reviewResponse.text()));
        }

        alert('Review added successfully.');



        // Send rating to /ratings endpoint
        const ratingResponse = await fetch(`${API_URL}ratings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ratingData),
        });

        if (!ratingResponse.ok) {
            throw new Error('Failed to add rating. ' + (await ratingResponse.text()));
        }

        alert('Rating added successfully.');

        // Redirect back to the movie list or another relevant page
        window.location.href = '/home.html';
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred: ' + error.message);
    }
});

// 팝업 열기
function openPopup() {
    document.getElementById('review-popup').style.display = 'block';
}

// 팝업 닫기
function closePopup() {
    document.getElementById('review-popup').style.display = 'none';
}