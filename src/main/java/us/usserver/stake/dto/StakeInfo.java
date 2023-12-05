package us.usserver.stake.dto;

import lombok.*;
import us.usserver.author.Author;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.stake.Stake;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StakeInfo {
    private Author author;
    private Float percentage;

    public static StakeInfo fromStake(Stake stake) {
        return StakeInfo.builder()
                .author(stake.getAuthor())
                .percentage(stake.getPercentage())
                .build();
    }
}
