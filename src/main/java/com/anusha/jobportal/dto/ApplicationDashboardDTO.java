package com.anusha.jobportal.dto;

import java.time.LocalDateTime;

import com.anusha.jobportal.entity.ApplicationStatus;

public class ApplicationDashboardDTO {

    private Long applicationId;
    private Long jobId;

    private String jobTitle;
    private String companyName;
    private String location;
    private String employmentType;

    private ApplicationStatus status;
    private LocalDateTime appliedDate;

    public ApplicationDashboardDTO() {
    }

    public ApplicationDashboardDTO(
            Long applicationId,
            Long jobId,
            String jobTitle,
            String companyName,
            String location,
            String employmentType,
            ApplicationStatus status,
            LocalDateTime appliedDate) {

        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.employmentType = employmentType;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}