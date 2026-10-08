# Job Portal & Application Tracking System

A full-stack web application that allows job seekers to search and apply for jobs, recruiters to post jobs and manage applicants, and administrators to manage users, jobs, and applications.

## 🚀 Project Overview

The Job Portal & Application Tracking System is designed to simplify the recruitment process by providing separate functionalities for Job Seekers, Recruiters, and Administrators.

### 👤 Job Seeker

- Register and login securely
- Browse available jobs
- Search jobs using keywords
- Filter jobs by location and employment type
- Apply for jobs
- Prevent duplicate applications
- Upload and manage resume
- Save and remove jobs
- Track application status
- View personal profile

### 🏢 Recruiter

- Secure recruiter login
- Post new job opportunities
- View jobs posted by the recruiter
- Update job details
- Delete jobs
- View applicants for posted jobs
- View applicant details
- Update application status
- Track recruitment progress

### 🛡️ Administrator

- Secure admin login
- View dashboard statistics
- Manage users
- Update user roles
- View all jobs
- Manage jobs
- View all applications
- View application statistics
- Monitor application statuses

## 🛠️ Technologies Used

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- REST API
- JWT Authentication
- Maven

### Database

- MySQL

### Frontend

- HTML5
- CSS3
- JavaScript

### Development Tools

- Eclipse IDE
- MySQL
- Postman
- Git
- GitHub

## 🔐 Security

The application uses Spring Security and JWT-based authentication.

- Secure login using JWT
- Passwords are encrypted using BCrypt
- Role-based authorization
- Separate access for Job Seekers, Recruiters, and Administrators
- Recruiters can manage only their own jobs and applicants
- Sensitive configuration such as database credentials and JWT secrets is kept outside the Git repository

## 📌 Application Roles

| Role | Main Responsibilities |
|------|-----------------------|
| Job Seeker | Search jobs, apply, save jobs, track applications |
| Recruiter | Post jobs, manage jobs, view applicants, update application status |
| Admin | Manage users, jobs, and applications |

## 🔄 Application Status

Applications can move through the following stages:

```text
APPLIED
   ↓
SHORTLISTED
   ↓
INTERVIEW
   ↓
SELECTED / REJECTED