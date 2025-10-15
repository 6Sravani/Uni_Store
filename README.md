# University Store - E-Commerce Platform

This repository contains the backend service for a full-stack e-commerce application for university merchandise. It is built with a robust, enterprise-grade Java and Spring Boot stack, featuring a complete token-based authentication system using JWT and a public-facing API for a complex product catalog.

---

## Backend Features & API Capabilities

The backend provides a complete and secure RESTful API for the User and Product domains.

* **Token-Based Authentication (JWT):** Full implementation of JWT for stateless authentication. Includes a secure flow with short-lived access tokens and long-lived refresh tokens stored in `HttpOnly` cookies.
* **Role-Based Access Control (RBAC):** Secure endpoints based on user roles (e.g., `USER` vs. `ADMIN`) using Spring Security's authorization rules.
* **Professional Architecture:** Built using a layered architecture (Controller, Service, Repository) with a Rich Domain Model.
* **Secure by Design:** Utilizes the Data Transfer Object (DTO) pattern and MapStruct to create a secure API contract, preventing internal data models from being exposed.
* **Robust Error Handling:** Features a centralized, global exception handler (`@RestControllerAdvice`) and custom security exception handlers to provide consistent and predictable JSON error responses.
* **Advanced Product Catalog:** A fully normalized schema to support products with complex variations (e.g., size, color), attributes, and multiple images.
* **High-Performance Queries:** Custom JPQL with `JOIN FETCH` is used to solve the N+1 query problem, ensuring efficient data retrieval.

---

## API Endpoints

The following is a summary of the core API endpoints.

### Authentication Endpoints (`/api/auth`)
These endpoints are public and handle the authentication lifecycle.

| HTTP Method | URL Path | Description |
| :--- | :--- | :--- |
| `POST` | `/register` | Registers a new user. |
| `POST` | `/login` | Authenticates a user, returns an access token in the body and a refresh token in a secure `HttpOnly` cookie. |
| `POST` | `/refresh` | Accepts a valid refresh token from a cookie and returns a new access token. |
| `POST` | `/logout` | Clears the refresh token cookie to securely log a user out. |

### User Endpoints (`/api/users`)
These endpoints are for an authenticated user to manage their own account.

| HTTP Method | URL Path | Description |
| :--- | :--- | :--- |
| `GET` | `/me` | Retrieves the profile of the currently logged-in user. |
| `PUT` | `/me` | Updates the profile (e.g., name) of the currently logged-in user. |

### Admin Endpoints (`/api/admin/users`)
These endpoints are protected and require an `ADMIN` role.

| HTTP Method | URL Path | Description |
| :--- | :--- | :--- |
| `GET` | `/` | Retrieves a paginated list of all users. |
| `GET` | `/{id}` | Retrieves a single user by their ID. |
| `PUT` | `/{id}` | Updates a user's details (e.g., role, status). |
| `DELETE` | `/{id}` | Deletes a user. |

### Product Catalog Endpoints
These endpoints are public and for browsing the store.

| HTTP Method | URL Path | Description |
| :--- | :--- | :--- |
| `GET` | `/api/categories` | Retrieves the hierarchical list of all categories. |
| `GET` | `/api/products` | Retrieves a paginated list of all products (supports `?page`, `?size`, `?sort`). |
| `GET` | `/api/products/{slug}` | Retrieves the full details of a single product by its slug. |

---

## Tech Stack

**Backend Technologies:**
* Java 21
* Spring Boot 3
* Spring Security (JWT)
* Spring Data JPA / Hibernate
* Lombok (for reducing boilerplate code)
* MapStruct (for efficient DTO mapping)
* Flyway (for database migrations)
* MySQL
* Maven

---

## Getting Started

Follow these instructions to get the backend server running on your local machine.

### Prerequisites

You will need the following tools installed on your system:
* [Git](https://git-scm.com/)
* [JDK 17 or later](https://www.oracle.com/java/technologies/downloads/) (This project uses JDK 21)
* [Apache Maven](https://maven.apache.org/download.cgi)
* [MySQL Server](https://dev.mysql.com/downloads/mysql/)

### IDE Configuration (IntelliJ IDEA)

This project uses **Lombok** to reduce boilerplate code. To ensure the IDE recognizes the generated methods, you must enable annotation processing:

1.  Go to `File > Settings` (or `IntelliJ IDEA > Preferences` on macOS).
2.  Navigate to `Build, Execution, Deployment > Compiler > Annotation Processors`.
3.  Check the box for **`Enable annotation processing`**.
4.  Apply the changes.

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/](https://github.com/)[YOUR_USERNAME]/university-store.git
    cd university-store
    ```

2.  **Configure the Database:**
    * Using a MySQL client, create the database schema:
        ```sql
        CREATE DATABASE uni_shop_db;
        ```
    * Navigate to the `Backend/store/src/main/resources/` directory.
    * Make a copy of the `application-template.yaml` file and rename it to **`application.yaml`**.
    * Open `application.yaml` and replace the placeholder values with your local MySQL credentials.

3.  **Configure JWT Secret:**
    * In the root of the backend project (`Backend/store/`), create a file named `.env`.
    * Inside this `.env` file, add your JWT secret key. Generate a long, secure key (e.g., using `openssl rand -base64 32`).
        ```
        JWT_SECRET="your-super-long-and-secure-key-pasted-here"
        ```

4.  **Run the Server:**
    * Open the `Backend/store/` directory as a project in your IDE.
    * Allow Maven to download all dependencies.
    * Run the main application (`StoreApplication.java`).
    * On the first run, Flyway will execute all necessary migrations to create the database schema.
    * The server is now running and available at `http://localhost:8080`.
---

## API Documentation

This project uses SpringDoc to automatically generate OpenAPI 3.0 documentation. Once the server is running, you can view and interact with the API at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## License

This project is licensed under the MIT License.