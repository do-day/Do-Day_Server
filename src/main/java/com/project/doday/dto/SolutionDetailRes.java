package com.project.doday.dto;

import com.project.doday.domain.Solution;
import com.project.doday.domain.SolutionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "신고 상세 모델")
public class SolutionDetailRes {

    @Schema(description = "해결 아이디" , example = "1")
    private Long solutionId;

    @Schema(description = "해결 신청 날짜" , example = "2023-08-01T09:03:23.924Z")
    private LocalDateTime createdDate;

    @Schema(description = "신고 날짜" , example = "2023-07-31T09:03:23.924Z")
    private LocalDateTime reportDate;

    @Schema(description = "신고 위도" , example = "37.5460")
    private Double latitude;

    @Schema(description = "신고 경도" , example = "127.1039")
    private Double longitude;

    @Schema(description = "신고 위치" , example = "서울시 광진구 화양동 7")
    private String location;

    @Schema(description = "빗물받이를 청소한 사진" , example = "")
    private String photo;

    @Schema(description = "허위 신고 제보" , example = "허위 신고인 것 같습니다.")
    private String falseReport;

    @Schema(description = "반려사유" , example = "허위 보고로 판단되어 반려되었습니다.")
    private String content;

    @Schema(description = "해결상태" , example = "RESOLVED")
    private SolutionState state;

    public Solution toEntity() {
        return Solution.builder()
                .location(location)
                .photo(photo)
                .build();
    }
}
