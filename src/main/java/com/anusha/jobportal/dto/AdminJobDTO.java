package com.anusha.jobportal.dto;

import java.time.LocalDateTime;

public class AdminJobDTO {

    private Long id;
    private String title;
    private String companyName;
    private String location;
    private String employmentType;
    private Double salary;
    private String experienceRequired;
    private String skills;
    private String recruiterEmail;
    private LocalDateTime postedDate;

    public AdminJobDTO() {
    }

    public AdminJobDTO(
            Long id,
            String title,
            String companyName,
            String location,
            String employmentType,
            Double salary,
            String experienceRequired,
            String skills,
            String recruiterEmail,
            LocalDateTime postedDate) {

        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.employmentType = employmentType;
        this.salary = salary;
        this.experienceRequired = experienceRequired;
        this.skills = skills;
        this.recruiterEmail = recruiterEmail;
        this.postedDate = postedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getExperienceRequired() {
        return experienceRequired;
    }

    public void setExperienceRequired(String experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }
}