package com.project.doday.repository;

import com.project.doday.domain.ReportReject;
import com.project.doday.domain.SolutionReject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRejectRepository extends JpaRepository<ReportReject, Long> {
    Optional<ReportReject> findByReportId(Long solutionId);
}

