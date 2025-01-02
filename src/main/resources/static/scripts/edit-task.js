const urlParams = new URLSearchParams(window.location.search);
const taskId = urlParams.get('taskId');
const projectId = urlParams.get('projectId');
document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');

    if (token && taskId && projectId) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;

            // Set the href for the Go Back link
            document.getElementById('go-back-link').href = `task.html?taskId=${taskId}&projectId=${projectId}`;

            // Fetch and display current task details
            await fetchAndDisplayTaskDetails(taskId, token);

            // Add event listener for edit task form submission
            document.getElementById('edit-task-form').addEventListener('submit', async function(event) {
                event.preventDefault();
                await editTask(taskId, projectId, token);
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

async function fetchAndDisplayTaskDetails(taskId, token) {
    try {
        const response = await fetch(`http://localhost:8080/api/project/${projectId}/task/${taskId}`, {
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

async function editTask(taskId, projectId, token) {
    const name = document.getElementById('task-name-input').value;
    const description = document.getElementById('task-description-input').value;
    const completed = document.getElementById('task-completed-input').checked;

    try {
        const response = await fetch(`http://localhost:8080/api/project/${projectId}/task/${taskId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, description, completed })
        });

        if (response.ok) {
            window.location.href = `task.html?taskId=${taskId}&projectId=${projectId}`;
        } else {
            console.error('Failed to update task:', response.statusText);
            alert('Failed to update task. Please try again later.');
        }
    } catch (error) {
        console.error('Error updating task:', error);
        alert('An error occurred. Please try again later.');
    }
}