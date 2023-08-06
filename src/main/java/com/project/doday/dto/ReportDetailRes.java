package com.project.doday.dto;

import com.project.doday.domain.ReportState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Schema(description = "신고 상세 모델")
public class ReportDetailRes {

    @Schema(description = "신고 아이디" , example = "1")
    private Long reportId;

    @Schema(description = "신고한 사람 아이디" , example = "1")
    private Long reportUserId;

    @Schema(description = "신고 위도" , example = "37.5460")
    private Double latitude;


    @Schema(description = "신고 경도" , example = "127.1039")
    private Double longitude;

    @Schema(description = "신고 위치" , example = "서울시 광진구 화양동 7")
    private String location;

    @Schema(description = "빗물받이 사진" , example = "")
    private String photoRaincatch;

    @Schema(description = "주변 환경 사진" , example = "")
    private String photoAround;

    @Schema(description = "신고 상세 설명" , example = "담배꽁초가 40개 이상 있는 것 같습니다.")
    private String description;

    @Schema(description = "신고 상태" , example = "UNRESOLVED")
    private ReportState state;

    @Schema(description = "반려사유" , example = "허위 신고로 판단되어 반려되었습니다.")
    private String content;
}
