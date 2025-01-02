# Task Manager

This is the back-end for an application that helps manage current projects and tasks associated with those projects.

## Technologies Used
- Java
- Spring Boot
- Spring Security with JWT
- Hibernate
- Maven
- HTML
- CSS
- JavaScript

## Features
- Handles most possible exceptions and sends them as `ResponseEntity`.
- Admin panel where the admin can CRUD other users tasks/projects.
- All projects/tasks are secured by exceptions, ensuring that if a user tries to access a project/task that does not belong to them, they will receive an error.

### Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

## Testing
- Unit tests written for most of the code.
- Tested with Postman.

## Usage
1. Configure the database (or implement h2 db).
2. Run the Spring Boot application.
3. Use the pre-created admin user (username: admin, password: admin).
4. Access the application by going to [http://localhost:8080](http://localhost:8080).

## API Endpoints
Here are some of the main API endpoints:

- `POST /api/auth/register`: Create an account ( Or use pre-initialized admin/admin ;) )
- `POST /api/auth/login`: Authenticate and receive a JWT token.
- `GET /api/projects`: Retrieve a list of projects.
- `POST /api/projects`: Create a new project.
- `GET /api/projects`: Get all projects for a current user.
- `PUT /api/projects/{id}`: Update a specific project by ID.
- `DELETE /api/projects/{id}`: Delete a specific project by ID.

## Contributing
Contributions are welcome! Please fork this repository and make a pull request with your changes.

## Contact
If you have any questions, feel free to open an issue or contact the repository owner.
