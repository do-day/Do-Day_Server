package com.project.doday.service;

import com.project.doday.domain.*;
import com.project.doday.dto.ReportFindAllRes;
import com.project.doday.dto.ReportRejectReq;
import com.project.doday.dto.SolutionRejectReq;
import com.project.doday.dto.SolutionReq;
import com.project.doday.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final SolutionRepository solutionRepository;
    private final SolutionRejectRepository solutionRejectRepository;
    private final ReportRepository reportRepository;
    private final ReportRejectRepository reportRejectRepository;

    /**
     * 해결한 신고 승인하기
     */
    public Solution approveSolution(Long solutionId, Long adminId) {
        Solution solution = solutionRepository.findById(solutionId).get();
        solution.setState(SolutionState.CONFIRMED);
        return solution;
    }

    /**
     * 해결한 신고 반려하기
     */
    public Solution rejectSolution(Long solutionId, Long adminId, SolutionRejectReq solutionRejectReq) {
        Solution solution = solutionRepository.findById(solutionId).get();
        Admin admin = adminRepository.findById(adminId).get();
        solution.setState(SolutionState.REJECTED);
        // TODO report의 state를 해결완료(RESOLVED)에서 미해결(UNRESOLVED)로 변경

        // 반려 내역 추가
        SolutionReject solutionReject = new SolutionReject(solutionId, admin, solutionRejectReq.getContent());
        solutionRejectRepository.save(solutionReject);

        return solution;
    }

    /**
     * 새로운 신고 승인하기
     */
    public Report approveReport(Long reportId) {
        Report report = reportRepository.findById(reportId).get();
        report.setState(ReportState.UNRESOLVED);
        return report;
    }

    /**
     * 새로운 신고 반려하기
     */
    public Report rejectReport(Long reportId, ReportRejectReq reportRejectReq) {
        Report report = reportRepository.findById(reportId).get();
        Admin admin = adminRepository.findById(reportRejectReq.getAdminId()).get();
        report.setState(ReportState.REJECTED);

        ReportReject reportReject = new ReportReject(reportId, admin, reportRejectReq.getContent());
        reportRejectRepository.save(reportReject);

        return report;
    }
}
