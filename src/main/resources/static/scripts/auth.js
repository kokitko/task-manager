document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = '../login.html';
    }
});