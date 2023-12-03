package us.usserver.novel.dto;

import lombok.Getter;
import us.usserver.novel.novelEnum.Orders;
import us.usserver.novel.novelEnum.Sorts;

@Getter
public class SortDto {
    private Sorts sorts;
    private Orders orders;
}
