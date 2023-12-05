package us.usserver.stake;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.usserver.global.ApiResponse;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;

@ResponseBody
@RestController
@RequestMapping("/stake")
@RequiredArgsConstructor
public class StakeController {
    private final StakeService stakeService;

    @GetMapping("/{novelId}")
    public ResponseEntity<ApiResponse<?>> getChapters(@PathVariable Long novelId) {
        List<StakeInfo> stakeInfos = stakeService.getStakeInfoOfNovel(novelId);

        ApiResponse<Object> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(stakeInfos)
                .build();
        return ResponseEntity.ok(response);
    }
}
