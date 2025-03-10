package lkd.namsic.cnkb.domain.item.repository;

import java.util.List;
import lkd.namsic.cnkb.domain.item.dto.InventoryItemDto;
import lkd.namsic.cnkb.enums.InventoryType;
import lkd.namsic.cnkb.enums.domain.ItemType;

public interface InventoryRepositoryCustom<T> {

    InventoryType getInventoryType();
    List<InventoryItemDto> getItems(T t, List<ItemType> priorityTypes);
    List<InventoryItemDto> getItems(T t, int page);
    int getItemCount(T user);
    int getItemCount(T t, ItemType itemType);
    InventoryItemDto getItem(T target, ItemType itemType);
    void setCount(T t, ItemType itemType, int count);
}
