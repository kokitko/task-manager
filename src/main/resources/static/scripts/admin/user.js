document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    let currentPage = 0;
    const pageSize = 6;

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;
            document.getElementById('create-project-link').href = `create-project.html?id=${id}`;

            await fetchAndDisplayProjects(currentPage, pageSize, token, id);

            document.getElementById('prev-page').addEventListener('click', async function() {
                if (currentPage > 0) {
                    currentPage--;
                    await fetchAndDisplayProjects(currentPage, pageSize, token, id);
                }
            });

            document.getElementById('next-page').addEventListener('click', async function() {
                currentPage++;
                await fetchAndDisplayProjects(currentPage, pageSize, token, id);
            });

        } catch (error) {
            console.error('Error parsing JWT token or fetching projects:', error);
            alert('An error occurred. Please try again later.');
        }
    } else {
        alert('You must be logged in to access this page.');
        window.location.href = '../login.html';
    }

    document.getElementById('logout-button').addEventListener('click', function() {
        localStorage.removeItem('jwtToken');
        window.location.href = '../../login.html';
    });
});

async function fetchAndDisplayProjects(page, size, token, id) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/user/${id}/projects?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const projectsData = await response.json();
            const projects = projectsData.projects;
            console.log('Projects:', projects);

            const projectsList = document.getElementById('projects-list');
            projectsList.innerHTML = '';
            projects.forEach(project => {
                const listItem = document.createElement('a');
                listItem.href = `project.html?id=${id}&projectId=${project.id}`;
                listItem.textContent = project.name;
                listItem.classList.add('clickable');
                projectsList.appendChild(listItem);
            });

            document.getElementById('prev-page').style.display = page > 0 ? 'inline-block' : 'none';
            document.getElementById('next-page').style.display = !(projectsData.last) ? 'inline-block' : 'none';
        } else {
            console.error('Failed to fetch projects:', response.statusText);
            alert('Failed to fetch projects. Please try again later.');
        }
    } catch (error) {
        console.error('Error fetching projects:', error);
        alert('An error occurred. Please try again later.');
    }
}