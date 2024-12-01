const RATING_API_URL = 'http://localhost:8080/ratings';

// Create Rating
document.getElementById('create-rating-form')?.addEventListener('submit', async (event) => {
    event.preventDefault();
    const movieId = document.getElementById('movie-id').value;
    const userId = document.getElementById('user-id').value;
    const rating = document.getElementById('rating').value;

    const response = await fetch(RATING_API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ movie: { id: movieId }, users: { id: userId }, rating }),
    });

    const result = await response.text();
    document.getElementById('output').textContent = result;
});

// Get Rating
document.getElementById('get-rating-form')?.addEventListener('submit', async (event) => {
    event.preventDefault();
    const ratingId = document.getElementById('rating-id').value;

    const response = await fetch(`${RATING_API_URL}/${ratingId}`);
    if (response.ok) {
        const result = await response.json();
        document.getElementById('output').textContent = JSON.stringify(result, null, 2);
    } else {
        document.getElementById('output').textContent = 'Rating not found.';
    }
});

// Delete Rating
document.getElementById('delete-rating-form')?.addEventListener('submit', async (event) => {
    event.preventDefault();
    const ratingId = document.getElementById('rating-id').value;

    const response = await fetch(`${RATING_API_URL}/${ratingId}`, {
        method: 'DELETE',
    });

    if (response.ok) {
        document.getElementById('output').textContent = 'Rating deleted successfully.';
    } else {
        document.getElementById('output').textContent = 'Failed to delete rating.';
    }
});
