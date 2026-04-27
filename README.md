<h1 align="center">
  Rentx | Wedding Decor Rental Platform 💍✨
</h1>

<p align="center">
  <strong>An enterprise-grade marketplace for high-end event decor management.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Angular-14+-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular" />
  <img src="https://img.shields.io/badge/Security-JWT_%26_OAuth2-black?style=for-the-badge&logo=json-web-tokens" alt="Security" />
  <img src="https://img.shields.io/badge/DevOps-Jenkins_%7C_GitLab-orange?style=for-the-badge&logo=jenkins" alt="DevOps" />
</p>

---

## 📖 Project Overview

**Rentx** is a specialized B2C rental platform designed to manage the unique complexities of wedding decor inventory. Built for scale and reliability, the application handles everything from visual asset management via Firebase to secure, delegated authorization using OAuth2.

## 🛠️ Enterprise Tech Stack

| Layer | Technology | Key Usage |
| :--- | :--- | :--- |
| **Frontend** | **Angular** | Reactive state management and modular component architecture. |
| **Styling** | **Tailwind CSS & Material** | Utility-first styling combined with robust UI components. |
| **Backend** | **Java / Spring Boot** | Secure, high-performance RESTful API. |
| **Database** | **MySQL** | ACID-compliant relational data management. |
| **Storage** | **Firebase** | Cloud-native storage for high-resolution event imagery. |
| **Security** | **JWT & OAuth2** | Stateless authentication and secure third-party authorization. |
| **CI/CD** | **Jenkins & GitLab** | Automated pipelines for continuous integration and delivery. |

---

## 🛡️ Engineering Excellence 

This project prioritizes maintainability, observability, and security through industry-standard patterns:

### ⚙️ Backend Architecture
- **Global Exception Handling:** Implemented a centralized `@ControllerAdvice` to ensure all API responses follow a consistent, predictable structure—even in failure states.
- **Observability & Logging:** Integrated standardized SLF4J/Logback logging. This ensures that production issues can be traced and debugged efficiently via clear, contextual logs.
- **Security Headers:** Configured Spring Security to mitigate common vulnerabilities (XSS, CSRF) while managing **JWT** and **OAuth2** flows.

### 🧪 Quality Assurance
- **Frontend (Jest):** Unit and integration tests to verify component behavior and state transitions in Angular.
- **Backend (JUnit 5 & Mockito):** Robust test suite focusing on business-critical logic and data integrity.

### 🚀 DevOps & Deployment
- **Automated Pipelines:** Fully integrated with **GitLab** and **Jenkins CI/CD** to automate the build-test-deploy lifecycle.
- **Production Environment:** Deployed on enterprise company servers, simulating a real-world infrastructure environment.

---

## 🚀 Installation & Setup

### Prerequisites
* Java 17+ & Maven
* Node.js 18+ & Angular CLI
* MySQL Instance

### Backend Setup
1. Clone the repository and navigate to `/backend`.
2. Create an `application.properties` file (or use environment variables) to provide your MySQL credentials and Firebase API keys.
3. Run: `./mvnw clean install` then `./mvnw spring-boot:run`

### Frontend Setup
1. Navigate to `/frontend`.
2. Run: `npm install`
3. Start development server: `npm start`
4. Run tests: `npm run test`

---

## 📈 Future Roadmap
- [ ] **Database Versioning:** Implementation of Liquibase or Flyway for schema migration control.
- [ ] **API Documentation:** Integration of OpenAPI/Swagger for interactive API exploration.
- [ ] **Real-time Notifications:** WebSocket integration for booking updates.

## 📄 License
This project is licensed under the MIT License.
