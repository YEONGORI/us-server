package us.usserver.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import us.usserver.author.Author;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JoinedAuthorInfo {
    private Author author;
    private Float rate;
}
