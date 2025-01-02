document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        const currentPath = window.location.pathname;
        if (currentPath === '/index.html' || currentPath === '/pages/login.html' || currentPath === '/pages/register.html') {
            window.location.href = '../pages/authenticated/menu.html';
        }
    }
});