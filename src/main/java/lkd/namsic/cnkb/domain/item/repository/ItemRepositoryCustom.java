package lkd.namsic.cnkb.domain.item.repository;

import lkd.namsic.cnkb.domain.item.Item;
import lkd.namsic.cnkb.enums.domain.ItemType;

public interface ItemRepositoryCustom {

    Item findByItemType(ItemType itemType);
}
