package com.project.doday.controller;

import com.project.doday.domain.Solution;
import com.project.doday.dto.ReportFindAllRes;
import com.project.doday.dto.SolutionRejectReq;
import com.project.doday.dto.SolutionReq;
import com.project.doday.repository.AdminRepository;
import com.project.doday.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * 해결한 신고 승인하기
     */
    @PutMapping("/solution/approval/{solutionId}/{adminId}")
    public ResponseEntity<Solution> approvalSolution(@PathVariable Long solutionId, @PathVariable Long adminId) {
        Solution solution = adminService.approveSolution(solutionId, adminId);
        return new ResponseEntity(solution, HttpStatus.OK);
    }

    /**
     * 해결한 신고 반려하기
     */
    @PutMapping("/solution/reject/{solutionId}/{adminId}")
    public ResponseEntity<Solution> rejectSolution(@PathVariable Long solutionId, @PathVariable Long adminId, @RequestBody SolutionRejectReq solutionRejectReq) {
        Solution solution = adminService.rejectSolution(solutionId, adminId, solutionRejectReq);
        return new ResponseEntity(solution, HttpStatus.OK);
    }

}
