package com.project.doday.service;

import com.project.doday.domain.Member;
import com.project.doday.domain.Reward;
import com.project.doday.repository.MemberRepository;
import com.project.doday.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final MemberRepository memberRepository;

    /**
     * 리워드 내역 확인하기
     */
    public List<Reward> getMyReward(Long memberId) {
        List<Reward> rewards = rewardRepository.findAll();
        List<Reward> myReward = new ArrayList<>();

        for(Reward reward : rewards){
            if(reward.getMember().getId() == memberId)
                myReward.add(reward);
        }
        return myReward;
    }

    /**
     * 리워드 전환하기
     */
    public Long convertReward(Long memberId, Long amount) {
        Member member = memberRepository.findById(memberId).get();
        member.covertReward(amount);
        return member.getNowReward();
    }
}
