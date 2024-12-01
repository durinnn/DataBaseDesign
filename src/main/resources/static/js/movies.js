const MOVIE_API_URL = 'http://localhost:8080/movies';

let movies = []; // 전역 배열

// 페이지 로드 시 영화 데이터 로드
document.addEventListener('DOMContentLoaded', loadMovieSummaries);

// 영화 추가
function addMovie() {
    const title = document.getElementById('title').value;
    const genre = document.getElementById('genre').value;
    const releaseDate = document.getElementById('release-date').value;

    if (!title || !genre || !releaseDate) {
        alert('모든 필드를 입력해주세요.');
        return;
    }

    fetch(MOVIE_API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, genre, releaseDate }),
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to add movie');
            return response.text();
        })
        .then(() => {
            alert('영화가 성공적으로 추가되었습니다.');
            loadMovieSummaries(); // 영화 목록 갱신
        })
        .catch(error => alert('영화 추가에 실패했습니다: ' + error.message));
}

// 영화 삭제
function deleteMovie() {
    const title = document.getElementById('movie-title-delete').value;
    const releaseDate = document.getElementById('movie-release-date-delete').value;

    if (!title || !releaseDate) {
        alert('Title과 Release Date를 모두 입력해주세요.');
        return;
    }

    fetch(`${MOVIE_API_URL}/delete`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, releaseDate }),
    })
        .then(response => {
            if (!response.ok) throw new Error('Failed to delete movie');
            alert('영화가 성공적으로 삭제되었습니다.');
            loadMovieSummaries(); // 영화 목록 갱신
        })
        .catch(error => alert('영화 삭제에 실패했습니다: ' + error.message));
}

// 영화 데이터 로드 및 초기 렌더링
function loadMovieSummaries() {
    fetch(`${MOVIE_API_URL}/summary`, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch movie summaries');
            return response.json();
        })
        .then(data => {
            movies = data; // 전역 변수에 저장
            renderMovies(movies); // 전체 영화 렌더링
        })
        .catch(error => console.error('Error loading movie summaries:', error));
}

// 영화 목록 렌더링
function renderMovies(movieArray) {
    const movieList = document.getElementById('movies');
    movieList.innerHTML = ""; // 기존 내용을 초기화

    movieArray.forEach(movie => {
        const li = document.createElement('li');
        li.innerHTML = `
            <a href="addReview.html?movieId=${movie.movieId}" class="movie-title"> 
            <strong>${movie.title}</strong></a>
            <p>Genre: ${movie.genre}</p>
            <p>Release Date: ${movie.releaseDate}</p>
            <p>Reviews: ${movie.reviewCount || 0}</p>
            <p>Average Rating: ${movie.avgRating ? movie.avgRating.toFixed(1) : 'N/A'}</p>
        `;
        movieList.appendChild(li);
    });
}

// 제목으로 필터링
function filterMovies() {
    const query = document.getElementById('search-box').value.toLowerCase();
    const filteredMovies = movies.filter(movie =>
        movie.title.toLowerCase().includes(query)
    );
    renderMovies(filteredMovies);
}

// 평점 높은 영화 필터링
function loadTopRatedMovies() {
    fetch(`${MOVIE_API_URL}/top-rated`, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch top-rated movies');
            return response.json();
        })
        .then(data => {
            renderMovies(data);
        })
        .catch(error => console.error('Error loading top-rated movies:', error));
}

// 특정 장르 영화 필터링
function loadMoviesByGenre() {
    const genre = document.getElementById('genre-filter').value;

    if (genre === "All") {
        renderMovies(movies);
        return;
    }

    fetch(`${MOVIE_API_URL}/genre/${genre}`, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch movies by genre');
            return response.json();
        })
        .then(data => {
            renderMovies(data);
        })
        .catch(error => console.error('Error loading movies by genre:', error));
}

// 이벤트 리스너 등록
document.getElementById('search-box').addEventListener('input', filterMovies);
document.getElementById('load-top-rated-btn').addEventListener('click', loadTopRatedMovies);
document.getElementById('load-by-genre-btn').addEventListener('click', loadMoviesByGenre);
// 팝업 열기
function openPopup(popupId) {
    const popup = document.getElementById(popupId);
    const overlay = document.getElementById('overlay');
    if (popup && overlay) {
        popup.style.display = 'block';
        overlay.style.display = 'block';
    }
}

// 팝업 닫기
function closePopup(popupId) {
    const popup = document.getElementById(popupId);
    const overlay = document.getElementById('overlay');
    if (popup && overlay) {
        popup.style.display = 'none';
        overlay.style.display = 'none';
    }
}

// 모든 팝업 닫기
function closeAllPopups() {
    const popups = document.querySelectorAll('.popup');
    const overlay = document.getElementById('overlay');
    popups.forEach(popup => (popup.style.display = 'none'));
    if (overlay) overlay.style.display = 'none';
}