document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    const payload = JSON.parse(atob(token.split('.')[1]));
    const role = payload.role;
    if (!token || role !== 'ROLE_ADMIN') {
        window.location.href = '../../login.html';
    }
});