package com.project.doday.service;

import com.project.doday.domain.*;
import com.project.doday.dto.SolutionDetailRes;
import com.project.doday.dto.SolutionListRes;
import com.project.doday.dto.SolutionReq;
import com.project.doday.repository.MemberRepository;
import com.project.doday.repository.ReportRepository;
import com.project.doday.repository.SolutionRejectRepository;
import com.project.doday.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SolutionService {
    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final SolutionRejectRepository solutionRejectRepository;
    private final S3Uploader s3Uploader;

    /**
     * 해결 신청하기
     */
    public Solution applySolution(Long reportId, Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        Report report = reportRepository.findById(reportId).get();

        Solution createSolution = Solution.builder()
                .member(member)
                .report(report)
                .latitude(report.getLatitude())
                .longitude(report.getLongitude())
                .location(report.getLocation())
                .photo("")
                .falseReport("")
                .state(SolutionState.RESOLVING)
                .reportDate(report.getCreatedDate())
                .build();
        solutionRepository.save(createSolution);
        report.setState(ReportState.RESOLVING);

        return createSolution;
    }

    /**
     * 해결한 문제 보고하기
     */
    public Solution reportSolution(Long solutionId, Long memberId, SolutionReq solutionReq) {
        Solution solution = solutionRepository.findById(solutionId).get();
        Report report = reportRepository.findById(solution.getReport().getId()).get();
        solution.setState(SolutionState.RESOLVED);
        report.setState(ReportState.RESOLVED);

        MultipartFile image = solutionReq.getPhoto();
        System.out.println("image = " + image);
        if(!image.isEmpty()) {
            String storedFileName = null;
            storedFileName = s3Uploader.upload("images", image);
            solution.setPhoto(storedFileName);
        }

        solution.setPhoto(solution.getPhoto());
        if (solutionReq.getFalseReport() != null) {
            solution.setFalseReport(solutionReq.getFalseReport());
        }

        return solution;
    }

    /**
     * 전체 해결 목록 확인
     */
    @Transactional(readOnly = true)
    public List<SolutionListRes> getSolutionList() {
        List<Solution> solutions = solutionRepository.findAllPriority();
        List<SolutionListRes> solutionListRes = new ArrayList<>();
        for(Solution solution : solutions) {
            // 반려 내역 불러오기
            Optional<SolutionReject> solutionReject = solutionRejectRepository.findBySolutionId(solution.getId());
            String content;
            if (solutionReject.isPresent()) {
                content = solutionReject.get().getContent();
            } else {
                content = "";
            }

            SolutionListRes solutionRes = new SolutionListRes(
                    solution.getId(), solution.getCreatedDate(),
                    solution.getReportDate(), solution.getLocation(),
                    solution.getPhoto(), content, solution.getState());

            solutionListRes.add(solutionRes);
        }
        return solutionListRes;
    }

    /**
     * 해결내역 상세보기
     */
    public SolutionDetailRes getSolution(Long solutionId) {
        Solution solution = solutionRepository.findById(solutionId).get();
        // 반려 내역 불러오기
        Optional<SolutionReject> solutionReject = solutionRejectRepository.findBySolutionId(solution.getId());
        String content;
        if (solutionReject.isPresent()) {
            content = solutionReject.get().getContent();
        } else {
            content = "";
        }

        SolutionDetailRes solutionDetailRes = new SolutionDetailRes(solutionId, solution.getCreatedDate(), solution.getReportDate(),
                solution.getLatitude(), solution.getLongitude(),
                solution.getLocation(), solution.getPhoto(), solution.getFalseReport(), content, solution.getState());
        return solutionDetailRes;
    }

}
