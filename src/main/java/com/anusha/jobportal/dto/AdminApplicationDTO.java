package com.anusha.jobportal.dto;

import java.time.LocalDateTime;

public class AdminApplicationDTO {

    private Long applicationId;

    private Long jobId;

    private String applicantName;

    private String applicantEmail;

    private String jobTitle;

    private String companyName;

    private String location;

    private String employmentType;

    private String status;

    private String coverLetter;

    private LocalDateTime appliedDate;

    public AdminApplicationDTO() {
    }

    public AdminApplicationDTO(
            Long applicationId,
            Long jobId,
            String applicantName,
            String applicantEmail,
            String jobTitle,
            String companyName,
            String location,
            String employmentType,
            String status,
            String coverLetter,
            LocalDateTime appliedDate) {

        this.applicationId = applicationId;
        this.jobId = jobId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.employmentType = employmentType;
        this.status = status;
        this.coverLetter = coverLetter;
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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}