package com.project.doday.controller;

import com.project.doday.dto.SolutionListRes;
import com.project.doday.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 나의 해결 목록 보기
     */
    @GetMapping("/mypage/solution/{memberId}")
    public List<SolutionListRes> getSolutionList(@PathVariable Long memberId) {
        List<SolutionListRes> solutionList = memberService.getMySolution(memberId);
        return solutionList;
    }

}
