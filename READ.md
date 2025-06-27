# 🛒 EasyShop E-Commerce API

EasyShop is a Spring Boot-based backend API for an e-commerce application. It connects to a MySQL database and is designed to serve a front-end interface or be tested using tools like Postman.

---

## 📚 Table of Contents

1. [Core Technologies](#core-technologies)
2. [API Endpoints Overview](#api-endpoints-overview)
    - [CategoriesController](#21-categoriescontroller)
    - [ProductsController](#22-productscontroller)
3. [Data Access Layer (DAO)](#data-access-layer-dao)
    - CategoryDao / MySqlCategoryDao
    - ProductDao / MySqlProductDao
4. [Key Spring Annotations](#key-spring-annotations--concepts-explained)
5. [JDBC Concepts](#jdbc-concepts-explained)

---

## 🧰 Core Technologies

- **Spring Boot** – Robust framework for rapid backend development
- **MySQL** – Relational database for persistent storage
- **JDBC** – API for executing SQL queries and handling results
- **Spring Security** – Role-based access control (e.g., ADMIN)
- **Postman** – API testing and debugging tool

---

## 🔌 API Endpoints Overview

### 2.1. `CategoriesController`

Manages all category-related operations.

- `GET /categories` — List all categories
- `GET /categories/{categoryId}/products` — List products in a category
- `POST /categories` — Add a new category (ADMIN only)
- `PUT /categories/{categoryId}` — Update a category (ADMIN only)
- `DELETE /categories/{categoryId}` — Delete a category (ADMIN only)

### 2.2. `ProductsController`

Handles product-related CRUD and search functionality.

- `GET /products` — Search products (filters: category, price, color)
- `GET /products/{productId}` — Get product by ID
- `POST /products` — Create a product (ADMIN only)
- `PUT /products/{productId}` — Update a product (ADMIN only)
- `DELETE /products/{productId}` — Delete a product (ADMIN only)

---

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

## 🧠 JDBC Concepts Explained

- `Connection`: Opens a session to the database.
- `PreparedStatement`: Used for parameterized queries.
- `ResultSet`: Handles results returned from a query.
- Exception handling is implemented to manage SQL failures gracefully.

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL Server
- Postman or a front-end interface


---

### 2.1. `CategoriesController`

This controller handles operations related to product categories.

#### ✅ Key Annotations Used

- **`@RestController`**  
  Combines `@Controller` and `@ResponseBody`. It designates the class as a controller where every method returns a domain object instead of a view — ideal for RESTful APIs that respond with JSON.

- **`@RequestMapping("categories")`**  
  Sets the base path for all endpoints in this controller to `/categories`.  
  - Example: `@GetMapping("")` maps to `GET /categories`.

- **`@CrossOrigin("*")`**  
  Enables **Cross-Origin Resource Sharing (CORS)**, allowing requests from any origin (e.g., from a frontend hosted elsewhere).  
  In production, replace `*` with specific trusted origins like `http://your-frontend.com`.

- **`@Autowired`**  
  Injects Spring-managed beans (e.g., `CategoryDao`, `ProductDao`) into the controller for database access or business logic.

#### 🧭 Common Mappings

- **`@GetMapping("")`**  
  Returns a list of all categories.  
  - URL: `GET /categories`

- **`@PostMapping`**  
  Adds a new category using data from the request body.  
  - Requires `@RequestBody Category category` to map JSON input to a Java object.  
  - URL: `POST /categories` (ADMIN only)

- **`@PutMapping("{categoryId}")`**  
  Updates a specific category by ID using the request body.  
  - URL: `PUT /categories/{categoryId}` (ADMIN only)

- **`@DeleteMapping("{categoryId}")`**  
  Deletes a category by ID after verifying it exists.  
  - URL: `DELETE /categories/{categoryId}` (ADMIN only)

- **`@GetMapping("{categoryId}/products")`**  
  Fetches all products belonging to a specific category.  
  - Uses `@PathVariable int categoryId` to extract the ID from the URL.  
  - URL: `GET /categories/{categoryId}/products`  
  - Example: `GET /categories/3/products` returns all products in category 3.

#### 📦 Parameter Binding

- **`@PathVariable`**  
  Binds a URL segment (e.g., `/categories/5`) to a method parameter.  
  - Example: `@GetMapping("{categoryId}")` with `@PathVariable int categoryId`

- **`@RequestBody`**  
  Converts incoming JSON in the HTTP request body into a Java object.  
  - Common in `POST` and `PUT` requests for creating or updating records.

