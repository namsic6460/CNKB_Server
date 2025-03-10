package lkd.namsic.cnkb.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lkd.namsic.cnkb.enums.domain.ItemType;

public record InventoryItemDto(
    ItemType itemType,
    Integer count
) {
    @QueryProjection
    public InventoryItemDto {}

    public record InventoryItemDtoListWithPriority(
        List<InventoryItemDto> priorityList,
        List<InventoryItemDto> dtoList
    ) {}
}
