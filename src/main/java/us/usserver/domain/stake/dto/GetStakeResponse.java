package us.usserver.domain.stake.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetStakeResponse {
    @Schema(description = "사용자 정보", example = "[지분 정보1, 지분 정보2, 지분 정보3, ...]")
    private List<StakeInfo> stakeInfos;
}
