document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    const projectId = urlParams.get('projectId');

    if (token && projectId) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;
            document.getElementById('go-back-btn').href = `project.html?id=${userId}&projectId=${projectId}`;

            document.getElementById('create-task-form').addEventListener('submit', async function (event) {
                event.preventDefault();
                await createTask(token, projectId, userId);
            });

        } catch (error) {
            console.error('Error parsing JWT token or fetching:', error);
            alert('An error occurred. Please try again later.');
        }
    } else {
        alert('You must be logged in and have a project ID to access this page.');
        window.location.href = '../login.html';
    }

    document.getElementById('logout-button').addEventListener('click', function() {
        localStorage.removeItem('jwtToken');
        window.location.href = '../login.html';
    });
});

async function createTask(token, projectId, userId) {
    const name = document.getElementById('task-name-input').value;
    const description = document.getElementById('project-description-input').value;
    const completed = false;

    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${userId}/projects/${projectId}/tasks`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`, 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, description, completed })
        });

        if (response.ok) {
            const task = await response.json();
            window.location.href = `project.html?id=${userId}&projectId=${projectId}`;
        } else {
            console.error('Failed to create task:', response.statusText);
            alert('Failed to create task. Please try again later.');
        }
    } catch (error) {
        console.error('Error creating task:', error);
        alert('An error occurred. Please try again later.');
    }
}