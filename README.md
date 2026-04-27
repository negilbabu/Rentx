<h1 align="center">
  Rentx | Wedding Decor Rental Ecosystem 💍✨
</h1>

<p align="center">
  <strong>A high-scale, full-stack marketplace connecting wedding vendors with planners and couples.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Angular-14+-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular" />
  <img src="https://img.shields.io/badge/Security-JWT_%7C_OAuth2-black?style=for-the-badge&logo=json-web-tokens" alt="Security" />
  <img src="https://img.shields.io/badge/Storage-Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" />
  <img src="https://img.shields.io/badge/CI%2FCD-Jenkins_%7C_GitLab-orange?style=for-the-badge&logo=jenkins" alt="DevOps" />
</p>

---

## 📖 Executive Summary

**Rentx** is a specialized B2C and B2B rental platform built to manage the logistical and visual complexities of the wedding decor industry. The project features three distinct user experiences (Admin, Vendor, and Customer) integrated into a unified backend, utilizing a cloud-native approach for asset management and a stateless security model.

## 🚀 Architectural Highlights

### 🛡️ Secure Backend (Java / Spring Boot)
The core API is designed with **Clean Architecture** and **SOLID** principles, ensuring that business logic is decoupled from external frameworks.
- **Stateless Security:** Implementation of **JWT** and **OAuth2** with custom filters for secure, scalable authentication.
- **Robust Validation:** Custom constraint annotations (e.g., `@Password`, `@CapitalLetter`) and DTO-level validation ensure data integrity before reaching the persistence layer.
- **Global Resilience:** A centralized `@ControllerAdvice` handles exceptions across the entire API, returning consistent RFC-7807 compliant error responses.
- **Relational Integrity:** MySQL-driven persistence with optimized JPA/Hibernate queries to handle complex many-to-many relationships (Orders, Products, and Stores).

### 🎨 Modular Frontends (Angular)
The client-side is split into two specialized applications—**Admin Console** and **User/Vendor Marketplace**—to ensure a lean bundle size and distinct security boundaries.
- **Reactive State Management:** Extensive use of **RxJS** and BehaviorSubjects for real-time UI updates (e.g., cart counts, sidebar state).
- **Onboarding UX:** A sophisticated multi-stage vendor registration flow to ensure high-quality marketplace participants.
- **Responsive Design:** A utility-first approach using **Tailwind CSS** combined with **Angular Material** for accessible, enterprise-grade components.

---

## 🛠️ Enterprise Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3, Spring Data JPA, Hibernate |
| **Frontend** | Angular 16+, RxJS, TypeScript, SCSS |
| **Security** | Spring Security, JWT, OAuth2, Google Sign-In |
| **Database** | MySQL (ACID compliant) |
| **Cloud Storage** | Firebase (for high-resolution visual assets) |
| **Testing** | **JUnit 5 & Mockito** (Backend), **Jest** (Frontend) |
| **DevOps** | Jenkins, GitLab CI/CD, Standardized SLF4J Logging |

---

## ✨ Key Features by Role

### 👤 For Customers
- **Visual Discovery:** Search and filter decor by category and sub-category.
- **Seamless Checkout:** Multi-address management and integrated order summary.
- **Wishlist & Cart:** Persistent state management for planning future events.

### 🏪 For Vendors
- **Store Management:** Create and manage individual digital storefronts.
- **Inventory Control:** Complex product management with support for high-res image uploads to Firebase.
- **Order Tracking:** Specialized dashboard to manage incoming rental requests and statuses.

### 🔑 For Administrators
- **Vendor Vetting:** Approval/Rejection workflows for new vendor applications.
- **Category Hierarchy:** Dynamic management of product taxonomies.
- **Platform Analytics:** Overview of users, vendors, and marketplace health.

---

## 🛡️ Engineering Excellence (Operational Focus)

As a senior-focused project, Rentx prioritizes what happens **after** the code is shipped:
- **Observability:** Standardized SLF4J/Logback logging with clear trace context for production debugging.
- **Security Headers:** Configured to mitigate XSS, Clickjacking, and CSRF attacks.
- **Performance:** Asynchronous image handling via Firebase to keep the primary database performant and lightweight.
- **Maintainability:** Separation of Concerns (SoC) between Controllers, Services, and Repositories with 100% interface-driven business logic.

---

## 🚀 Getting Started

### Prerequisites
* JDK 17 & Maven 3.x
* Node.js 18+ (LTS)
* MySQL 8.x

### Backend Setup
1. Clone the repo: `git clone https://github.com/your-username/rentx.git`
2. Navigate to `server/`
3. Configure `src/main/resources/application.properties` with your database and Firebase credentials.
4. Build & Run: `./mvnw spring-boot:run`

### Frontend Setup
1. Navigate to `client/user/` (Marketplace) or `client/admin/` (Admin Panel).
2. Install dependencies: `npm install`
3. Start development server: `npm start`
4. Run tests: `npm run test` (Jest)

---

## 📈 Future Roadmap
- [ ] **Database Migrations:** Integrating Flyway/Liquibase for schema versioning.
- [ ] **Real-time Notifications:** WebSockets for instant order and approval alerts.
- [ ] **API Documentation:** Full Swagger/OpenAPI 3 implementation.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
