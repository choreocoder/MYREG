# Church Registration & Attendance System

A full-stack application built to streamline administrative workflows, manage midweek small groups, and track congregational attendance metrics. The system leverages a robust **Role-Based Access Control (RBAC)** architecture to provide distinct, tailored user experiences for Administrators, Small Group Leaders, and Pastors.

---

## 💡 Why This Project?

1. **The Elective Requirement:** Built as the core technical entry for my chosen elective track at my institution.
2. **Real-World Impact:** Designed to solve a tangible, paper-heavy problem for my local church community using modern technology.
3. **End-to-End Mastery:** A personal challenge to fully understand full-stack development—specifically how distinct frontend, backend, and data layers communicate with one another in a live environment.

---

## 📈 Reflection: Highlights & Lowlights

### ⚡ Highlights
* **Systems & Aesthetics:** Blending my systems-thinking side to construct a solid relational database architecture, while simultaneously channeling my love for clean design on the frontend interface.
* **Proximity to the Problem:** Getting to build and deploy actual technical solutions for real-world problems that are close and meaningful to me.
* **The "Conceptual Thread":** Seeing abstract concepts connect seamlessly in practice—specifically tracing the threads between **Object-Oriented Programming (OOP)** paradigms in Java and **Relational Database** structures in MySQL.

### ⚠️ Lowlights
* **Structural Constraints:** Due to a highly compressed development deadline, there was no room to implement a formal, concise Agile development framework. 
* **Scaling on the Fly:** While the "learning-by-doing" approach is incredibly effective for rapid skill acquisition, managing that velocity is challenging to scale when building a complete architecture from scratch.

---

## 🗓️ Project Timeline

### **Week 1: Foundation & Data Architecture ✅**
* Conducted core research on full-stack application architectures and relational database paradigms.
* Explored SQL syntax, data normalization practices, and relational use cases.
* Brainstormed system workflows and mapped entity relationships.
* Physically constructed and finalized the database schema using **MySQL Workbench**.

### **Week 2: Backend Logic & Frontend Hookup 🚧 (In Progress)**
* Developing the **Java core systems** and writing internal database APIs.
* Initializing the frontend interface using **Dart**.
* Integrating the components to make the frontend, backend, and database talk to one another smoothly.

---

## 🛠️ Tech Stack

* **Backend:** Java (Core Logic & API Control Layer)
* **Database Interface:** JDBC (Java Database Connectivity)
* **Database Engine:** MySQL 8.0 (Fully normalized relational schema)
* **Frontend:** Dart 
* **Environment:** Containerized Development via Podman / Distrobox

---

## 📊 Database Schema Summary

The database layer is normalized to ensure data consistency, eliminate redundancy, and protect relational links using strict foreign key constraints (`ON DELETE CASCADE` and `ON DELETE RESTRICT`).

* **`people`**: The master directory tracking all individuals (Members, Visitors, Staff).
* **`visitors`**: Specialized tracking metadata for first-time attendees.
* **`leaders`**: Manages small group leadership data and experience tiers (`trainee`, `first tier`, `senior`).
* **`pastors`**: Tracks pastoral roles (`senior`, `associate`, `youth`).
* **`departments` & `assignments`**: Handles internal staff staffing, role assignments, and oversight.
* **`cell_groups`**: Maps physical small groups to assigned leaders and managing pastors.
* **`cell_members`**: A many-to-many bridge table connecting people to their midweek cell groups without duplication.
* **`cell_attendance_logs`**: Tracks midweek small group presence history.
* **`sunday_attendance_logs`**: Handles service-specific individual check-ins at the door during corporate Sunday services.
