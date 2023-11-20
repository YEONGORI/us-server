package us.usserver.like.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.like.likeEnum.LikeType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetLikeReq {
    @NotNull
    private Long id;

    @NotNull
    private LikeType likeType;
}
