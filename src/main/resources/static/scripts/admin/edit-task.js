
document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get('id');
    const taskId = urlParams.get('taskId');
    const projectId = urlParams.get('projectId');

    if (token && taskId && projectId) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;

            document.getElementById('go-back-link').href = `task.html?id=${userId}&taskId=${taskId}&projectId=${projectId}`;

            await fetchAndDisplayTaskDetails(taskId, token, projectId, userId);

            document.getElementById('edit-task-form').addEventListener('submit', async function(event) {
                event.preventDefault();
                await editTask(taskId, projectId, token, userId);
            });

        } catch (error) {
            console.error('Error parsing JWT token or fetching task details:', error);
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

async function fetchAndDisplayTaskDetails(taskId, token, projectId, userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${userId}/projects/${projectId}/tasks/${taskId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const task = await response.json();
            document.getElementById('task-name-input').value = task.name;
            document.getElementById('task-description-input').value = task.description;
            document.getElementById('task-completed-input').checked = task.completed;
        } else {
            console.error('Failed to fetch task details:', response.statusText);
            alert('Failed to fetch task details. Please try again later.');
        }
    } catch (error) {
        console.error('Error fetching task details:', error);
        alert('An error occurred. Please try again later.');
    }
}

async function editTask(taskId, projectId, token, userId) {
    const name = document.getElementById('task-name-input').value;
    const description = document.getElementById('task-description-input').value;
    const completed = document.getElementById('task-completed-input').checked;

    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${userId}/projects/${projectId}/tasks/${taskId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, description, completed })
        });

        if (response.ok) {
            window.location.href = `task.html?id=${userId}&taskId=${taskId}&projectId=${projectId}`;
        } else {
            console.error('Failed to update task:', response.statusText);
            alert('Failed to update task. Please try again later.');
        }
    } catch (error) {
        console.error('Error updating task:', error);
        alert('An error occurred. Please try again later.');
    }
}