document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    const urlParams = new URLSearchParams(window.location.search)
    const projectId = urlParams.get('projectId');
    const id = urlParams.get('id');
    let currentPage = 0;
    const pageSize = 5;

    if (token && projectId) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;

            document.getElementById('username-display').textContent = `Hello, ${username}`;

            await fetchAndDisplayProjectDetails(projectId, token, id);

            await fetchAndDisplayTasks(projectId, currentPage, pageSize, token, id);

            document.getElementById('prev-page').addEventListener('click', async function() {
                if (currentPage > 0) {
                    currentPage--;
                    tasks = await fetchAndDisplayTasks(projectId, currentPage, pageSize, token, id);
                }
            });

            document.getElementById('next-page').addEventListener('click', async function() {
                currentPage++;
                tasks = await fetchAndDisplayTasks(projectId, currentPage, pageSize, token, id);
            });

            document.getElementById('delete-project-button').addEventListener('click', async function() {
                await deleteProject(projectId, token, id);
            });

        } catch (error) {
            console.error('Error parsing JWT token or fetching project details:', error);
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

async function fetchAndDisplayProjectDetails(projectId, token, id) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${id}/projects/${projectId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const project = await response.json();
            document.getElementById('project-name').textContent = project.name;
            document.getElementById('project-description').textContent = project.description;
            document.getElementById('go-back-button').href = `user.html?id=${id}`;

            document.getElementById('edit-project-button').href = `edit-project.html?id=${id}&projectId=${projectId}`;
            document.getElementById('create-task-button').href = `create-task.html?id=${id}&projectId=${projectId}`;
        } else {
            console.error('Failed to fetch project details:', response.statusText);
            alert('Failed to fetch project details. Please try again later.');
        }
    } catch (error) {
        console.error('Error fetching project details:', error);
        alert('An error occurred. Please try again later.');
    }
}

async function fetchAndDisplayTasks(projectId, page, size, token, id) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${id}/projects/${projectId}/tasks?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const tasksData = await response.json();
            const tasks = tasksData.tasks;

            const tasksList = document.getElementById('tasks-list');
            tasksList.innerHTML = '';
            tasks.forEach(task => {
                const listItem = document.createElement('a');
                listItem.href = `task.html?id=${id}&taskId=${task.id}&projectId=${projectId}`;
                listItem.className = 'clickable';
                listItem.textContent = task.name;
                tasksList.appendChild(listItem);
            });

            document.getElementById('prev-page').style.display = page > 0 ? 'inline-block' : 'none';
            document.getElementById('next-page').style.display = !(tasksData.last) ? 'inline-block' : 'none';
        
            return tasks;
        } else {
            console.error('Failed to fetch tasks:', response.statusText);
            alert('Failed to fetch tasks. Please try again later.');
        }
    } catch (error) {
        console.error('Error fetching tasks:', error);
        alert('An error occurred. Please try again later.');
    }
}

async function deleteProject(projectId, token, id) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${id}/projects/${projectId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status === 204) {
            window.location.href = 'menu.html';
        } else {
            console.error('Failed to delete project:', response.statusText);
            alert('Failed to delete project. Please try again later.');
        }
    } catch (error) {
        console.error('Error deleting project:', error);
        alert('An error occurred. Please try again later.');
    }
}