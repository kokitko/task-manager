document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('jwtToken');
    let currentPage = 0;
    const pageSize = 10;

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.username || payload.sub || payload.name;
            document.getElementById('username-display').textContent = `Hello, ${username}`;

            await fetchAndDisplayUsers(currentPage, pageSize, token);

            document.getElementById('prev-page').addEventListener('click', async function() {
                if (currentPage > 0) {
                    currentPage--;
                    await fetchAndDisplayUsers(currentPage, pageSize, token);
                }
            });

            document.getElementById('next-page').addEventListener('click', async function() {
                currentPage++;
                await fetchAndDisplayUsers(currentPage, pageSize, token);
            });

        } catch (error) {
            console.error('Error parsing JWT token or fetching users:', error);
            alert('An error occurred. Please try again later.');
        }
    } else {
        alert('You must be logged in to access this page.');
        window.location.href = '../../login.html';
    }
    
    document.getElementById('logout-button').addEventListener('click', function() {
        localStorage.removeItem('jwtToken');
        window.location.href = '../../login.html';
    });
});

async function fetchAndDisplayUsers (page, size, token) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/users?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const usersData = await response.json();
            const users = usersData.users;
            console.log('Users:', users);

            const usersList = document.getElementById('users-list');
            usersList.innerHTML = '';
            users.forEach(user => {
                const listItem = document.createElement('a');
                listItem.href = `user.html?id=${user.id}`;
                listItem.classList.add('clickable');
                listItem.textContent = user.username;
                usersList.appendChild(listItem);
            });

            document.getElementById('prev-page').style.display = page > 0 ? 'inline-block' : 'none';
            document.getElementById('next-page').style.display = !(usersData.last) ? 'inline-block' : 'none';
        } else {
            console.error('Error fetching users:', error);
            alert('An error occurred. Please try again later');
        }
    } catch (error) {
        console.error('Error fetching users:', error);
        alert('An error occurred. Please try again later.');
    }
}