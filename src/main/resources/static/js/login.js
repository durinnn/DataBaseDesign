// API URL 설정
const LOGIN_API_URL = 'http://localhost:8080/users/login';

// 로그인 폼 제출 이벤트 처리
document.getElementById('login-form')?.addEventListener('submit', async (event) => {
    event.preventDefault(); // 기본 폼 제출 동작 방지

    // 입력된 값 가져오기
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username || !password) {
        document.getElementById('output').textContent = "Please fill in all fields.";
        return;
    }

    try {
        // 로그인 API 요청
        const response = await fetch(LOGIN_API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }), // JSON 데이터로 변환하여 전송
        });

        if (response.ok) {
            // 로그인 성공
            const result = await response.json(); // JSON으로 응답 처리
            document.getElementById('output').textContent = "Login successful";

            // 로그인 정보를 저장
            const user = { id: result.id, username: result.username }; // 응답 데이터에서 id와 username 추출
            localStorage.setItem('loggedInUser', JSON.stringify(user));

            // 로그인 성공 시 다른 페이지로 리디렉션
            setTimeout(() => {
                window.location.href = '/home.html'; // 홈 페이지로 이동
            }, 1000);
    }

    else if (response.status === 401) {
            // 로그인 실패 (잘못된 자격 증명)
            document.getElementById('output').textContent = "Invalid username or password.";
        } else {
            // 기타 오류
            document.getElementById('output').textContent = "An error occurred during login.";
        }
    } catch (error) {
        // 네트워크 오류 처리
        console.error('Error:', error);
        document.getElementById('output').textContent = "Failed to connect to the server.";
    }
});

// 페이지 로드 시 로그인 상태 확인 및 UI 업데이트
document.addEventListener('DOMContentLoaded', () => {
    const authButtons = document.getElementById('auth-buttons');

    // 로그인 상태 확인
    const user = JSON.parse(localStorage.getItem('loggedInUser'));
    console.log('Logged-in user:', user); // 디버깅용 출력

    if (user) {
        // 로그인 상태: 마이페이지와 로그아웃 버튼 표시
        authButtons.innerHTML = `
            <a href="../users/mypage.html"><button>My Page</button></a>
            <button onclick="logout()">Logout</button>
        `;
    } else {
        // 비로그인 상태: 로그인 버튼 표시
        authButtons.innerHTML = `
            <a href="../users/login.html"><button>Login</button></a>
        `;
    }
});

// 로그아웃 처리
function logout() {
    localStorage.removeItem('loggedInUser'); // 로그인 정보 제거
    alert('You have logged out successfully.');
    window.location.reload(); // 페이지 새로고침
}
