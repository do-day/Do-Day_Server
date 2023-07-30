package com.project.doday.controller;

import com.project.doday.domain.Reward;
import com.project.doday.dto.SolutionListRes;
import com.project.doday.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    /**
     * 리워드 내역 확인하기
     */
    @GetMapping("/reward/{memberId}")
    public ResponseEntity getSolutionList(@PathVariable Long memberId) {
        List<Reward> rewards = rewardService.getMyReward(memberId);
        return ResponseEntity.ok(rewards);
    }

    /**
     * 리워드 전환하기
     */
    @PutMapping("/reward/convert/{memberId}")
    public Long convertReward(@PathVariable Long memberId, Long amount) {
        Long convertReward = rewardService.convertReward(memberId, amount);
        return convertReward;
    }
}
