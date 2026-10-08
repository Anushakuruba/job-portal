package com.anusha.jobportal.dto;

public class RecruiterDashboardDTO {

    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private String employmentType;
    private long totalApplications;

    public RecruiterDashboardDTO() {
    }

    public RecruiterDashboardDTO(
            Long jobId,
            String jobTitle,
            String companyName,
            String location,
            String employmentType,
            long totalApplications) {

        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.location = location;
        this.employmentType = employmentType;
        this.totalApplications = totalApplications;
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

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }
}