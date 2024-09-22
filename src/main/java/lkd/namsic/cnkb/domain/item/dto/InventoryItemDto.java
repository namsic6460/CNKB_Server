package lkd.namsic.cnkb.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lkd.namsic.cnkb.enums.ItemType;

import java.util.List;

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
