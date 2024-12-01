const API_URL = 'http://localhost:8080/users';

// Create User
document.getElementById('create-user-form')?.addEventListener('submit', async (event) => {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, username, email, password }),
    });

    const result = await response.text();
    document.getElementById('output').textContent = result;
});

async function getUser() {
    const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));
    if (!loggedInUser || !loggedInUser.id) {
        document.getElementById('output').textContent = '로그인된 사용자가 없습니다.';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${loggedInUser.id}`);
        if (response.ok) {
            const result = await response.json();
            document.getElementById('output').textContent = JSON.stringify(result, null, 2);
        } else {
            document.getElementById('output').textContent = '사용자 정보를 불러오지 못했습니다.';
        }
    } catch (error) {
        console.error('오류 발생:', error);
        document.getElementById('output').textContent = '오류가 발생했습니다.';
    }
}
async function deleteUser() {
    const loggedInUser = JSON.parse(localStorage.getItem('loggedInUser'));
    if (!loggedInUser || !loggedInUser.id) {
        document.getElementById('output').textContent = '로그인된 사용자가 없습니다.';
        return;
    }

    if (confirm('정말로 계정을 삭제하시겠습니까?')) {
        try {
            const response = await fetch(`${API_URL}/${loggedInUser.id}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                document.getElementById('output').textContent = '계정이 성공적으로 삭제되었습니다.';
                localStorage.removeItem('loggedInUser'); // 로그아웃 처리
                setTimeout(() => {
                    window.location.href = '/home.html'; // 홈 페이지로 이동
                }, 2000);
            } else {
                document.getElementById('output').textContent = '계정 삭제에 실패했습니다.';
            }
        } catch (error) {
            console.error('오류 발생:', error);
            document.getElementById('output').textContent = '오류가 발생했습니다.';
        }
    }
}



