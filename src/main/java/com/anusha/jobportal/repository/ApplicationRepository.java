package com.anusha.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.anusha.jobportal.entity.Application;
import com.anusha.jobportal.entity.ApplicationStatus;
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    boolean existsByJobIdAndApplicantEmail(
            Long jobId,
            String applicantEmail
    );

    List<Application> findByApplicantEmail(
            String applicantEmail
    );

    List<Application> findByJobId(
            Long jobId
    );

    long countByJobId(
            Long jobId
    );
    long countByStatus(ApplicationStatus status);
    
    @Transactional
    void deleteByJobId(Long jobId);
}