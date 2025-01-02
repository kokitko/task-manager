document.getElementById('register-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, email: email, password: password })
        });

        if (response.ok) {
            // Automatically log in the user after successful registration
            const loginResponse = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username: username, password: password })
            });

            if (loginResponse.ok) {
                const token = await loginResponse.text();
                console.log('JWT Token received:', token);
                localStorage.setItem('jwtToken', token);
                window.location.href = 'authenticated/menu.html';
            } else {
                const loginErrorData = await loginResponse.json();
                console.error('Login failed:', loginErrorData);
                alert('Login failed after registration. Please try logging in manually.');
            }
        } else {
            const errorData = await response.json();
            console.error('Registration failed:', errorData);
            alert('Registration failed. Please check your details and try again.');
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('An error occurred. Please try again later.');
    }
});