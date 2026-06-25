
# 🔐 JWT Authentication System

A secure **Spring Boot-based authentication system** using **JSON Web Tokens (JWT)** with role-based access control.

---

## 🚀 Features

- User Registration & Login
- JWT Token Generation & Validation
- Role-Based Access Control (USER / ADMIN)
- Secure REST APIs
- Password Encryption using BCrypt
- Spring Security Integration

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Maven

---

## 🏗️ System Architecture

Client (Postman / Frontend)
↓
Spring Boot Backend
↓
JWT Authentication Filter
↓
Database (MySQL)

---

## 📂 Project Structure

src/
├── controller
├── service
├── repository
├── model
├── security
└── config

---

## ⚙️ Setup Instructions

### 1. Clone the repository

git clone https://github.com/Amol841189/jwt-auth-system.git
cd jwt-auth-system

### 2. Configure application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/jwt_db

spring.datasource.username=root

spring.datasource.password=YOUR_PASSWORD

jwt.secret=YOUR_SECRET_KEY

### 3. Run the application

mvn spring-boot:run
---

## 🔐 API Endpoints

### Authentication

| Method | Endpoint           | Description          |
| ------ | ------------------ | -------------------- |
| POST   | /api/auth/register | Register new user    |
| POST   | /api/auth/login    | Login user & get JWT |

### User APIs

| Method | Endpoint             | Role  |
| ------ | -------------------- | ----- |
| GET    | /api/user/profile    | USER  |
| GET    | /api/admin/dashboard | ADMIN |

---

## 🧪 Example Request

### Login Request

{
  "username": "amol",
  "password": "1234"
}

### Response

{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}

---

## 🔒 Security Features

* JWT-based authentication
* Password encryption (BCrypt)
* Stateless session management
* Role-based endpoint protection
* Rate Limiting to access Api

---

## 📌 Future Improvements

* Refresh Token Support
* OAuth2 Login (Google/GitHub)
* Docker Support
* API Rate Limiting
* Microservices conversion

---

## 👨‍💻 Author

* Name: Dnyaneshwar R. Chaudhari
* Project: JWT Authentication System
* Purpose: Learning + Resume Project

---


---

