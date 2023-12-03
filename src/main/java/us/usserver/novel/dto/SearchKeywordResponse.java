package us.usserver.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordResponse {
    List<String> recentSearch;
    List<String> hotSearch;
}
