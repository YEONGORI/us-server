package us.usserver.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.novel.novelEnum.Orders;
import us.usserver.novel.novelEnum.Sorts;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortDto {
    private Sorts sorts;
    private Orders orders;
}
