document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    const urlParams = new URLSearchParams(window.location.search)
    const userId = urlParams.get('id');

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;
            document.getElementById('go-back-link').href = `user.html?id=${userId}`;

            document.getElementById('create-project-form').addEventListener('submit', async function(event) {
                event.preventDefault();
                await createProject(token, userId);
            });

        } catch (error) {
            console.error('Error parsing JWT token:', error);
            alert('An error occurred. Please try again later.');
        }
    } else {
        alert('You must be logged in to access this page.');
        window.location.href = '../login.html';
    }

    document.getElementById('logout-button').addEventListener('click', function() {
        localStorage.removeItem('jwtToken');
        window.location.href = '../login.html';
    });
});

async function createProject(token, userId) {
    const name = document.getElementById('project-name-input').value;
    const description = document.getElementById('project-description-input').value;

    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${userId}/projects`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, description })
        });

        if (response.ok) {
            const project = await response.json();
            window.location.href = `project.html?id=${userId}&projectId=${project.id}`;
        } else {
            console.error('Failed to create project:', response.statusText);
            alert('Failed to create project. Please try again later.');
        }
    } catch (error) {
        console.error('Error creating project:', error);
        alert('An error occurred. Please try again later.');
    }
}