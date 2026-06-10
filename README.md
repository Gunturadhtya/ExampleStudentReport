
# Student Report

Sistem Pelaporan Kerusakan Fasilitas Kampus terintegrasi yang dirancang untuk memudahkan mahasiswa dalam melaporkan masalah fasilitas (seperti kerusakan AC, proyektor, kelistrikan, dll), serta memudahkan pihak administrator/teknisi untuk melacak dan mengelola perbaikan.

Sistem ini menggunakan arsitektur *API-driven* di mana *frontend* (Thymeleaf + Vanilla JS) berkomunikasi langsung dengan REST API *backend* (Spring Boot) secara *asynchronous*.

## Tim Pengembang

* **Muhammad Guntur Ricky Adhitya** – API, Database, Service, Middleware, Auth, Docker
* **Noor Khalisa** – UI Implementation dengan Bootstrap, Frontend Logic (JavaScript), Thymeleaf, API Integration
* **Nazla Salsabila** – Model, UI/UX

## Fitur Utama

### Mahasiswa (User)
* **Manajemen Akun:** Registrasi, Login, dan manajemen data akademik mahasiswa.
* **Dashboard Personal:** Melacak statistik laporan pribadi (Total, Diproses, Selesai).
* **Pembuatan Laporan:** Membuat laporan kerusakan yang dilengkapi dengan fitur *upload* foto maksimal 3 gambar (terintegrasi dengan MinIO).
* **Feed Laporan (Infinite Scroll):** Melihat laporan dari seluruh mahasiswa dengan fitur *infinite scrolling* dan filter pencarian.
* **Upvote System:** Memberikan dukungan (*upvote*) pada laporan mahasiswa lain agar menjadi prioritas.

### Administrator
* **Dashboard Admin:** *Overview* status seluruh laporan masuk (*Pending, In Progress, Completed*).
* **Manajemen Status Laporan:** Memperbarui status penanganan laporan beserta catatan teknis.
* **Master Data Management:** Operasi CRUD (Create, Read, Update, Delete) untuk referensi data:
  * Kategori Laporan (Infrastruktur, Kelistrikan, dll)
  * Gedung & Ruangan
* **Report Audit Log:** Melacak riwayat perubahan status pada setiap laporan.

## Tech Stack

### Backend
* **Language:** Kotlin, Java 17
* **Framework:** Spring Boot
* **Security:** Spring Security (Custom Session-based Token Authentication)
* **ORM:** Spring Data JPA / Hibernate
* **Database Migration:** Flyway
* **Relational Database:** PostgreSQL
* **Object Storage:** MinIO (S3-compatible storage for image uploads)

### Frontend
* **Template Engine:** Thymeleaf
* **Styling:** Bootstrap 5.3.8, Custom CSS
* **Icons:** Bootstrap Icons
* **Scripting:** Vanilla JavaScript (DOM manipulation, Fetch API, FormData)

### Infrastructure & Tools
* **Build Tool:** Gradle (Kotlin DSL)
* **Containerization:** Docker, Docker Compose
* **API Documentation:** OpenAPI 3.0 (YAML specification included)

## Color Reference

| Color             | Hex                                                                |
| ----------------- | ------------------------------------------------------------------ |
| **Main Background** (Login, Main Content) | ![#d4ebf8](https://dummyimage.com/10/d4ebf8/white?text=+) `#d4ebf8` |
| **Primary Button** (Tombol Submit) | ![#bde0fe](https://dummyimage.com/10/bde0fe/white?text=+) `#bde0fe` |
| **Button Border** (Garis Tombol) | ![#a2d2ff](https://dummyimage.com/10/a2d2ff/white?text=+) `#a2d2ff` |
| **Dark Text** (Teks Utama, Heading) | ![#0f172a](https://dummyimage.com/10/0f172a/white?text=+) `#0f172a` |
| **Secondary Accent** (Ikon, Placeholder) | ![#64748b](https://dummyimage.com/10/64748b/white?text=+) `#64748b` |
## API Reference

**Base URL:** `/api/v1`

**Authentication:** Bearer Token required for protected routes (`Authorization: Bearer <token>`).

**Idempotency:** `Idempotency-Key` header (UUID v4) supported on `POST`, `PUT`, and `PATCH` requests for safe retries.

---

### Auth

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `POST` | `/auth/register` | Register a new student account | No |
| `POST` | `/auth/login` | Login and obtain a Bearer token | No |
| `POST` | `/auth/logout` | Logout and invalidate the current session token | Yes |
| `GET` | `/auth/me` | Get the currently authenticated user | Yes |

---

### Users

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/users/me` | Get own profile | Yes |
| `PUT` | `/users/me` | Update own profile (name / email) | Yes |
| `PATCH` | `/users/me/password` | Change own password | Yes |
| `GET` | `/users/me/stats` | Get own report statistics | Yes |
| `GET` | `/users` | List all users | Yes (Admin) |
| `GET` | `/users/{id}` | Get user by ID | Yes (Admin) |
| `PUT` | `/users/{id}` | Update user by ID | Yes (Admin) |
| `DELETE` | `/users/{id}` | Delete user by ID | Yes (Admin) |
| `GET` | `/users/{id}/stats` | Get report statistics for a specific user | Yes (Admin) |

---

### Student Data

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/users/me/student-data` | Get own student data | Yes |
| `PATCH` | `/users/me/student-data` | Update own student data | Yes |
| `GET` | `/users/{id}/student-data` | Get student data for a user | Yes (Admin) |
| `PATCH` | `/users/{id}/student-data` | Update student data for a user | Yes (Admin) |

---

### Reports

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/reports` | List reports (paginated, supports filters) | Yes |
| `POST` | `/reports` | Create a new report (Initial status: Pending) | Yes |
| `GET` | `/reports/{id}` | Get report by ID | Yes |
| `PUT` | `/reports/{id}` | Update report content (Allowed if Pending) | Yes |
| `DELETE` | `/reports/{id}` | Soft-delete report | Yes |
| `PATCH` | `/reports/{id}/status` | Update report status | Yes (Admin) |

---

### Report Images

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/reports/{id}/images` | List images attached to a report | Yes |
| `POST` | `/reports/{id}/images` | Upload images for a report (Max 5MB each) | Yes |
| `DELETE` | `/reports/{id}/images/{imageId}` | Delete a specific image from a report | Yes |

---

### Buildings

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/buildings` | List all buildings (paginated) | Yes |
| `POST` | `/buildings` | Create a new building | Yes (Admin) |
| `GET` | `/buildings/{id}` | Get building by ID | Yes |
| `PUT` | `/buildings/{id}` | Update building | Yes (Admin) |
| `DELETE` | `/buildings/{id}` | Delete building | Yes (Admin) |
| `GET` | `/buildings/{id}/rooms` | List all rooms in a building | Yes |

---

### Rooms

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/rooms` | List all rooms (paginated, supports filters) | Yes |
| `POST` | `/rooms` | Create a new room | Yes (Admin) |
| `GET` | `/rooms/{id}` | Get room by ID | Yes |
| `PUT` | `/rooms/{id}` | Update room | Yes (Admin) |
| `DELETE` | `/rooms/{id}` | Delete room | Yes (Admin) |

---

### Categories

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/categories` | List all categories | Yes |
| `POST` | `/categories` | Create a new category | Yes (Admin) |
| `GET` | `/categories/{id}` | Get category by ID | Yes |
| `PUT` | `/categories/{id}` | Update category | Yes (Admin) |
| `DELETE` | `/categories/{id}` | Delete category | Yes (Admin) |

---

### Upvotes

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/reports/{id}/upvotes` | Get upvote summary and current-user status | Yes |
| `POST` | `/reports/{id}/upvotes` | Upvote a report (Once per user) | Yes |
| `DELETE` | `/reports/{id}/upvotes` | Remove own upvote from a report | Yes |

---

### Report Log

| Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `GET` | `/reports/{id}/logs` | Get audit log for a specific report | Yes |
| `GET` | `/report-logs` | List all report log entries | Yes (Admin) |
| `GET` | `/report-logs/{id}` | Get a single log entry by ID | Yes (Admin) |
