# University Store - E-Commerce Platform

This repository contains the backend service for a full-stack e-commerce application for university merchandise. It is built with a robust, enterprise-grade Java and Spring Boot stack.

---

## Tech Stack

**Backend Technologies:**
* Java 17+
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA / Hibernate
* MySQL
* Maven

---

## Getting Started

Follow these instructions to get the backend server running on your local machine.

### Prerequisites

You will need the following tools installed on your system:
* [Git](https://git-scm.com/)
* [JDK 17 or later](https://www.oracle.com/java/technologies/downloads/)
* [Apache Maven](https://maven.apache.org/download.cgi)
* [MySQL Server](https://dev.mysql.com/downloads/mysql/)

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
    * Make a copy of the `application-template.yaml` file and rename the copy to **`application.yaml`**.
    * Open the new `application.yaml` and replace the placeholder values with your local MySQL database credentials:
        * `YOUR_DATABASE_NAME` as uni_shop_db
        * `YOUR_DATABASE_USERNAME`
        * `YOUR_DATABASE_PASSWORD`

3.  **Run the Server:**
    * Open the `Backend/store/` directory as a project in your IDE (e.g., IntelliJ IDEA).
    * Allow Maven to download all the required dependencies.
    * Run the main application (`StoreApplication.java`).
    * The server is now running and available at `http://localhost:8080`.
---

## API Documentation

This project uses SpringDoc to automatically generate OpenAPI 3.0 documentation for the backend API. Once the server is running, you can view and interact with the API at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## License

This project is licensed under the MIT License.