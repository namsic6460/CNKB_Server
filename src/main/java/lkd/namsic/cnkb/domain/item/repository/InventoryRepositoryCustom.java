package lkd.namsic.cnkb.domain.item.repository;

import lkd.namsic.cnkb.domain.item.dto.InventoryItemDto;
import lkd.namsic.cnkb.enums.InventoryType;
import lkd.namsic.cnkb.enums.ItemType;

import java.util.List;

public interface InventoryRepositoryCustom<T> {

    InventoryType getInventoryType();
    List<InventoryItemDto> getItems(T t, List<ItemType> priorityTypes);
    List<InventoryItemDto> getItems(T t, int page);
    int getItemCount(T user);
    int getItemCount(T t, ItemType itemType);
    InventoryItemDto getItem(T target, ItemType itemType);
    void setCount(T t, ItemType itemType, int count);
}
