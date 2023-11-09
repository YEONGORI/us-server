package us.usserver.novel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@ResponseBody
@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {
    private final NovelService novelService;

    @GetMapping("/{novelId}")
    public ResponseEntity<?> getNovelInfo(
            @PathVariable Long novelId
    ) {

    }
    public ResponseEntity<?> createNovelSection(
            @PathVariable Long novelId,
            @Validated @RequestBody NovelSectionCreateDTO createDTO,
            HttpServletResponse response
    ) {
        Long memberId = getMemberId();
        NovelSectionResponseDTO novelSectionResponseDTO = novelSectionService.createNovelSection(novelId, memberId, createDTO);
        return makeResponseEntity(novelSectionResponseDTO, HttpStatus.CREATED);
    }
}
