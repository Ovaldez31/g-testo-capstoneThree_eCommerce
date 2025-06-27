# 🛒 EasyShop E-Commerce API

EasyShop is a Spring Boot-based backend API for an e-commerce application. It connects to a MySQL database and is designed to serve a front-end interface or be tested using tools like Postman.


---
## 🧰 Core Technologies
- **Java 17**
- **Apache Maven** (for dependency management and build automation)
- **Spring Boot** – Robust framework for rapid backend development
- **MySQL** – Relational database for persistent storage
- **JDBC** – API for executing SQL queries and handling results
- **Spring Security** – Role-based access control (e.g., ADMIN)
- **Postman** – API testing and debugging tool

---
## 🚀 Getting Started
To get the EasyShop API up and running on your local machine, follow these steps.

### Steps:

1.  **Locate the Database Script:**
    Find the database creation script, named `create_database.sql`. This file is located in a `database/sql` or `src/main/resources/sql` folder within the project directory.

2.  **Execute the SQL Script:**
    Open your preferred MySQL client and connect to your MySQL server. Then, execute the `create_database.sql` script. This script will:
  * Create a new database named `easyshop`.
  * Define all necessary tables (e.g., `categories`, `products`, `users`, etc.).
  * Populate these tables with initial sample data, including categories, products, and sample user accounts.

---

## 🔌 API Endpoints Overview

### 1. `CategoriesController`

Manages all category-related operations.

- `GET /categories` — List all categories
- `GET /categories/{categoryId}/products` — List products in a category
- `POST /categories` — Add a new category (ADMIN only)
- `PUT /categories/{categoryId}` — Update a category (ADMIN only)
- `DELETE /categories/{categoryId}` — Delete a category (ADMIN only)

### 2. `ProductsController`

Handles product-related CRUD and search functionality.

- `GET /products` — Search products (filters: category, price, color)
- `GET /products/{productId}` — Get product by ID
- `POST /products` — Create a product (ADMIN only)
- `PUT /products/{productId}` — Update a product (ADMIN only)
- `DELETE /products/{productId}` — Delete a product (ADMIN only)

---
## 🧩 Key Spring Annotations & Concepts Explained

- `@RestController`: Combines `@Controller` and `@ResponseBody` for RESTful APIs.
- `@RequestMapping`: Sets base URL path for a controller.
- `@CrossOrigin("*")`: Enables CORS from any origin.
- `@Autowired`: Injects Spring-managed beans (e.g., DAOs).
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: HTTP method handlers.
- `@PathVariable`: Binds URI values to method parameters.
- `@RequestBody`: Maps JSON payloads to Java objects.
- `@PreAuthorize("hasRole('ADMIN')")`: Restricts access to endpoints based on user roles.

---
## 🧪 CRUD Operations Explained

CRUD is an acronym for **Create**, **Read**, **Update**, and **Delete** — the four basic operations for managing persistent data in an application. These operations directly map to HTTP methods and SQL statements in RESTful APIs.

---

### 🟢 1. Create

- **Purpose:** Add new data to the database.
- **HTTP Method:** `POST`
- **SQL Command:** `INSERT`
 
 ### 🔵 2. Read
- **Purpose:** Retrieve existing data.
- **HTTP Method:** `GET`
- **SQL Command:** `SELECT`


### 🟠 3. Update
- **Purpose:** Modify existing data.
- **HTTP Method:** `PUT`
- **SQL Command:** `UPDATE`


### 🔴 4. Delete
- **Purpose:** Remove data from the database.
- **HTTP Method:** `DELETE`
- **SQL Command:** `DELETE`

## 🗃️ Data Access Layer (DAO)

The DAO layer handles interaction with the MySQL database using raw JDBC.

### Interfaces
- `CategoryDao`
- `ProductDao`

### Implementations
- `MySqlCategoryDao`
- `MySqlProductDao`

Each DAO class:
- Connects to the database using `DataSource`
- Prepares and executes SQL queries
- Maps result sets to model objects (`Product`, `Category`)

---

## 🧠 JDBC Concepts Explained

- `Connection`: Opens a session to the database.
- `PreparedStatement`: Used for parameterized queries.
- `ResultSet`: Handles results returned from a query.
- Exception handling is implemented to manage SQL failures gracefully.

---



