package us.usserver.novel.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordResponse {
    List<String> recentSearch;
    List<String> hotSearch;
}
