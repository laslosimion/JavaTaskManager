# JavaTaskManager
REST API (built with Spring Boot) to create and manage tasks

# User Story
As a user of the task management system, I want to use a REST API (built with
Spring Boot) to create and manage tasks linked to my account So that I can
track my work and update progress easily.

# End Goal
Deliver a Spring Boot backend with:
Core Entities
● User
○ id (UUID), name, email
● Task
○ id (UUID), title, description, status (TODO, IN_PROGRESS, DONE),
userId
Required Features
● REST API with CRUD for users and tasks
● Validation:
○ valid unique email
○ required title (1–140 chars)
○ Status codes: 201 Created, 200 OK, 204 No Content, 400 Bad
Request, 404 Not Found, 409 Conflict (invalid transition)

● Status transitions:

○ Allowed: TODO → IN_PROGRESS → DONE
○ Disallowed: backward or skipping transitions (return 409)

● Database: Spring Data JPA with H2 in-memory DB
# Acceptance Criteria
● POST /users → Create user, validate email, return 201
● GET /users/{id} → Return user or 404
● POST /tasks → Create task for existing user, validate title, return 201
● GET /tasks?userId=... → Return all tasks for that user
● PUT /tasks/{id} → Update status, respect transitions, return 200 or
404
● DELETE /tasks/{id} → Delete task, return 204 or 404
● Validation errors return 400 with clear error payload
Bonus (optional)
● JWT auth (/auth/register, /auth/login) → only logged-in users can
manage their tasks
● Pagination + filtering (GET
/tasks?userId=...&status=...&page=0&size=10)
● Swagger/OpenAPI docs at /swagger-ui
