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
    private final RewardRepository rewardRepository;
    private final MemberRepository memberRepository;

    /**
     * 해결한 신고 승인하기
     */
    public Solution approveSolution(Long solutionId, Long adminId) {
        Solution solution = solutionRepository.findById(solutionId).get();
        solution.setState(SolutionState.CONFIRMED); // 태그를 확인완료로 변경

        // TODO 리워드 적립금액 정하기 (여기서는 100원으로 고정)
        Reward reward = Reward.builder()
                .member(solution.getMember())
                .price(100L)
                .type("해결하기")
                .location(solution.getLocation())
                .build();
        rewardRepository.save(reward);
        Member member = memberRepository.findById(solution.getMember().getId()).get();
        member.earnReward(reward.getPrice());

        return solution;
    }

    /**
     * 해결한 신고 반려하기
     */
    public Solution rejectSolution(Long solutionId, Long adminId, SolutionRejectReq solutionRejectReq) {
        Solution solution = solutionRepository.findById(solutionId).get();
        Report report = reportRepository.findById(solution.getReport().getId()).get();
        Admin admin = adminRepository.findById(adminId).get();
        solution.setState(SolutionState.REJECTED);

        // report의 state를 해결완료(RESOLVED)에서 미해결(UNRESOLVED)로 변경
        report.setState(ReportState.UNRESOLVED);

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

        // 리워드 적립
        Reward reward = Reward.builder()
                .member(report.getMember())
                .price(100L)
                .type("신고하기")
                .location(report.getLocation())
                .build();
        rewardRepository.save(reward);
        Member member = memberRepository.findById(report.getMember().getId()).get();
        member.earnReward(reward.getPrice());

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
