document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    const urlParams = new URLSearchParams(window.location.search);
    const projectId = urlParams.get('projectId');

    if (token && projectId) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;

            // Set the href for the Go Back link
             document.getElementById('go-back-link').href = `project.html?projectId=${projectId}`;

            // Fetch and display current project details
            await fetchAndDisplayProjectDetails(projectId, token);

            // Add event listener for edit project form submission
            document.getElementById('edit-project-form').addEventListener('submit', async function(event) {
                event.preventDefault();
                await editProject(projectId, token);
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

async function fetchAndDisplayProjectDetails(projectId, token) {
    try {
        const response = await fetch(`http://localhost:8080/api/projects/${projectId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const project = await response.json();
            document.getElementById('project-name-input').value = project.name;
            document.getElementById('project-description-input').value = project.description;
        } else {
            console.error('Failed to fetch project details:', response.statusText);
            alert('Failed to fetch project details. Please try again later.');
        }
    } catch (error) {
        console.error('Error fetching project details:', error);
        alert('An error occurred. Please try again later.');
    }
}

async function editProject(projectId, token) {
    const name = document.getElementById('project-name-input').value;
    const description = document.getElementById('project-description-input').value;

    try {
        const response = await fetch(`http://localhost:8080/api/projects/${projectId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name, description })
        });

        if (response.ok) {
            window.location.href = `project.html?projectId=${projectId}`;
        } else {
            console.error('Failed to update project:', response.statusText);
            alert('Failed to update project. Please try again later.');
        }
    } catch (error) {
        console.error('Error updating project:', error);
        alert('An error occurred. Please try again later.');
    }
}