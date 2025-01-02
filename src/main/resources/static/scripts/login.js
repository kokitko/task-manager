document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: password })
        });

        if (response.ok) {
            const token = await response.text();
            console.log('JWT Token received:', token);
            localStorage.setItem('jwtToken', token);
            window.location.href = 'authenticated/menu.html';
        } else {
            const errorData = await response.json();
            console.error('Login failed:', errorData);
            alert('Login failed. Please check your credentials and try again.');
        }
    } catch (error) {
        console.error('Error during login:', error);
        alert('An error occurred. Please try again later.');
    }
});