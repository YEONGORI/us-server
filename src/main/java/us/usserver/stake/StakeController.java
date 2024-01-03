package us.usserver.stake;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiCsResponse;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/stake")
@RequiredArgsConstructor
public class StakeController {
    private final StakeService stakeService;

    @GetMapping("/{novelId}")
    public ResponseEntity<ApiCsResponse<?>> getChapters(@PathVariable Long novelId) {
        List<StakeInfo> stakeInfos = stakeService.getStakeInfoOfNovel(novelId);

        ApiCsResponse<Object> response = ApiCsResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(stakeInfos)
                .build();
        return ResponseEntity.ok(response);
    }
}
