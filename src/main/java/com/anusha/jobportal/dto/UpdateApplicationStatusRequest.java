package com.anusha.jobportal.dto;

import com.anusha.jobportal.entity.ApplicationStatus;

public class UpdateApplicationStatusRequest {

    private ApplicationStatus status;

    public UpdateApplicationStatusRequest() {
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}