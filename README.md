# 💰 Expense Tracker App

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green) ![React Native](https://img.shields.io/badge/React_Native-Expo-blue) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Supabase-336791)

A full-stack expense management application that allows users to securely sign up, log in, add transactions, categorize expenses/income, and view monthly summaries.

The system is designed with a **Spring Boot** REST API backend (deployed on Render) backed by **Supabase PostgreSQL**, and a **React Native (Expo)** mobile frontend.

---

## 🚀 Features

### 🔐 Authentication
* **Secure Access:** JWT-based authentication (Stateless).
* **User Management:** Secure signup and login endpoints.
* **Security:** BCrypt password hashing and Spring Security integration.

### 💸 Transaction Management
* **Record Keeping:** Add Income and Expense records easily.
* **Categorization:** Built-in categories (Food, Rent, Travel, Salary, etc.).
* **Filtering:**
    * By Type (Income vs Expense)
    * By Category
    * By Date Range
* **Summaries:** Get monthly financial summaries to track spending habits.

---

## 🛠️ Tech Stack

### Backend
* **Language:** Java 17
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security + JWT
* **ORM:** Hibernate / JPA
* **Database:** PostgreSQL (Hosted on Supabase)
* **Build Tool:** Gradle
* **Deployment:** Render

### Frontend
* **Framework:** React Native (Expo)
* **Runtime:** Node.js 20+
* **HTTP Client:** Axios
* **Storage:** Async Storage (for token management)

---

## 📡 API Endpoints

All secured endpoints require the header: `Authorization: Bearer <JWT_TOKEN>`

### Authentication
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/signup` | Register a new user |
| `POST` | `/api/auth/login` | Authenticate and receive JWT |

### Transactions
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/transactions` | Add a new transaction |
| `GET` | `/api/transactions` | Get all transactions |
| `GET` | `/api/transactions/type/{type}` | Filter by `INCOME` or `EXPENSE` |
| `GET` | `/api/transactions/category/{cat}` | Filter by category name |
| `GET` | `/api/transactions/date-range` | Filter by date (`?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`) |
| `GET` | `/api/transactions/summary/{y}/{m}` | Get summary for specific year/month |
| `GET` | `/api/transactions/summary/current` | Get summary for the current month |

---

## 🧩 Local Setup Guide

### Prerequisites
* **Java 17** SDK installed.
* **Node.js** (v18+, v20 Recommended).
* **Expo CLI** installed globally.
* **PostgreSQL** database (Local or Supabase credentials).

### 1. Backend Setup
1.  Clone the repository:
    ```bash
    git clone [https://github.com/your-username/ExpenseTrackerApp.git](https://github.com/your-username/ExpenseTrackerApp.git)
    cd backend
    ```
2.  Configure database in `src/main/resources/application.properties`:
    ```ini
    spring.datasource.url=jdbc:postgresql://<SUPABASE_HOST>:<PORT>/<DB_NAME>
    spring.datasource.username=<YOUR_DB_USERNAME>
    spring.datasource.password=<YOUR_DB_PASSWORD>
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```
3.  Run the application:
    ```bash
    ./gradlew bootRun
    ```
    *Server will start at `http://localhost:8080`*

### 2. Frontend Setup
1.  Navigate to the frontend directory:
    ```bash
    cd frontend
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Configure API URL in `src/services/api.js`:
    ```javascript
    // For local testing (Android Emulator uses 10.0.2.2 instead of localhost)
    export const BASE_URL = "http://localhost:8080"; 
    
    // For Production/Device testing
    // export const BASE_URL = "[https://YOUR-BACKEND.onrender.com](https://YOUR-BACKEND.onrender.com)";
    ```
4.  Start the app:
    ```bash
    npx expo start
    ```
    *Scan the QR code with Expo Go or press `a` for Android Emulator.*

---

## 🌍 Deployment

### Backend (Render)
The Spring Boot application is deployed on [Render](https://render.com).
* **Live URL:** `https://YOUR-APP-NAME.onrender.com`

### Database (Supabase)
PostgreSQL database is hosted on [Supabase](https://supabase.com).

---

## 🔮 Future Enhancements
- [ ] **Dashboard Analytics:** Visual charts and graphs for spending trends.
- [ ] **Budget Limits:** Set monthly limits and get alerts.
- [ ] **Export:** Download reports as PDF/CSV.
- [ ] **Notifications:** Push notifications for daily reminders.
- [ ] **UI Polish:** Dark mode and animation improvements.
