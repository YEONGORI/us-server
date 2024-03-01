package us.usserver.domain.authority.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.domain.authority.dto.res.StakeInfoResponse;
import us.usserver.domain.authority.service.StakeService;
import us.usserver.global.ApiCsResponse;
import us.usserver.global.UsApiResponse;
import us.usserver.global.exception.NovelNotFoundException;

@Tag(name = "지분 API")
@ResponseBody
@RestController
@RequestMapping("/stake")
@RequiredArgsConstructor
public class StakeController {
    private final StakeService stakeService;

    @Operation(summary = "지분 정보 조회", description = "특정 소설에 대한 지분 정보 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공",
                    content = @Content(schema = @Schema(implementation = StakeInfoResponse.class))),
            @ApiResponse(responseCode = "400", description = "소설이 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = NovelNotFoundException.class)))
    })
    @GetMapping("/{novelId}")
    public ResponseEntity<UsApiResponse<?>> getStakes(@PathVariable Long novelId) {
        StakeInfoResponse stakeInfos = stakeService.getStakeInfoOfNovel(novelId);

        UsApiResponse<Object> response = UsApiResponse.builder()
                .status(HttpStatus.OK)
                .message(HttpStatus.OK.getReasonPhrase())
                .data(stakeInfos)
                .build();
        return ResponseEntity.ok(response);
    }
}
