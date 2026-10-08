package com.anusha.jobportal.dto;

import java.time.LocalDateTime;

import com.anusha.jobportal.entity.ApplicationStatus;

public class RecruiterApplicationDTO {

    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;

    public RecruiterApplicationDTO() {
    }

    public RecruiterApplicationDTO(
            Long applicationId,
            String applicantName,
            String applicantEmail,
            ApplicationStatus status,
            LocalDateTime appliedDate) {

        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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