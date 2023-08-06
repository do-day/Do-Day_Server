package com.project.doday.service;

import com.project.doday.domain.*;
import com.project.doday.dto.ReportDetailRes;
import com.project.doday.dto.ReportFindAllRes;
import com.project.doday.dto.ReportReq;
import com.project.doday.dto.SolutionDetailRes;
import com.project.doday.repository.AdminRepository;
import com.project.doday.repository.MemberRepository;
import com.project.doday.repository.ReportRejectRepository;
import com.project.doday.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final ReportRejectRepository reportRejectRepository;
    private final S3Uploader s3Uploader;

    /**
     * 문제 신고하기
     */
    public Long createReport(ReportReq reportReq) {
        Member member = memberRepository.findById(reportReq.getMemberId()).get();

        String rcImage = "";
        String arImage = "";

        MultipartFile raincatchImage = reportReq.getPhotoRaincatch();
        MultipartFile aroundImage;
        if(reportReq.getPhotoAround() != null){
            aroundImage = reportReq.getPhotoAround();
            arImage = s3Uploader.upload("images", aroundImage);
        }

        System.out.println(raincatchImage.toString());
        if(!raincatchImage.isEmpty()) {
            rcImage = s3Uploader.upload("images", raincatchImage);
        }


        Report createReport = Report.builder()
                .member(member)
                .latitude(reportReq.getLatitude())
                .longitude(reportReq.getLongitude())
                .location(reportReq.getLocation())
                .photoRaincatch(rcImage)
                .photoAround(arImage)
                .description(reportReq.getDescription())
                .state(ReportState.UNAPPROVAL)
                .build();

        reportRepository.save(createReport);
        return createReport.getId();

    }

    /**
     * 신고 목록 보기 - 사용자
     */
    public ArrayList<ReportFindAllRes> findAllReport(){
        ArrayList<ReportFindAllRes> findResult = new ArrayList<ReportFindAllRes>();

        ArrayList<Report> reports = reportRepository.findAllByStateOrderByCreatedDateDesc(ReportState.UNRESOLVED);

        for(Report report: reports) {
            findResult.add(new ReportFindAllRes(report.getId(), report.getLocation(), report.getPhotoRaincatch(), report.getCreatedDate(), report.getState()));
        }
        return findResult;
    }

    /**
     * 전체 신고 목록 확인 - 관리자 (승인 전)
     */
    public ArrayList<ReportFindAllRes> findAdminReport(){
        ArrayList<ReportFindAllRes> findAdminReport = new ArrayList<ReportFindAllRes>();

        ArrayList<Report> reports = reportRepository.findAllByStateOrderByCreatedDateDesc(ReportState.UNAPPROVAL);

        for(Report report: reports) {
            findAdminReport.add(new ReportFindAllRes(report.getId(), report.getLocation(), report.getPhotoRaincatch(), report.getCreatedDate(), report.getState()));
        }
        return findAdminReport;
    }


    /**
     * 신고 상세 보기
     */
    public ReportDetailRes getReport(Long reportId) {
        Report report = reportRepository.findById(reportId).get();

        Optional<ReportReject> reportReject = reportRejectRepository.findByReportId(report.getId());

        String content;
        if (reportReject.isPresent()) {
            content = reportReject.get().getContent();
        } else {
            content = "";
        }

        ReportDetailRes reportDetailRes = new ReportDetailRes(reportId, report.getMember().getId(), report.getLatitude(), report.getLongitude(),
                report.getLocation(), report.getPhotoRaincatch(), report.getPhotoAround(), report.getDescription(), report.getState(), content);
        return reportDetailRes;
    }
}
