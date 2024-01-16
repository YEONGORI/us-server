package us.usserver.author.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthorReq {
    private String profileImg;
    @Size(max = 11)
    private String nickname;
    @Size(max = 100)
    private String introduction;
    private Boolean participateNovelsPublic;
    private Boolean collectionNovelsPublic;
}
