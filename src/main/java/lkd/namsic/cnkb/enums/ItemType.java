package lkd.namsic.cnkb.enums;

import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ItemType {

    LOWEST_ORE_STONE("최하급 광석"),
    LOW_ORE_STONE("하급 광석"),
    MIDDLE_ORE_STONE("중급 광석"),
    SAND("모래"),
    STONE("돌"),
    COAL("석탄"),
    COPPER("구리"),
    ;

    private static final Map<String, ItemType> itemTypeMap = new HashMap<>();

    public static ItemType find(String itemName) {
        ItemType itemType = itemTypeMap.get(itemName);
        if (itemType == null) {
            throw DataNotFoundException.item();
        }

        return itemType;
    }

    static {
        for (ItemType itemType : ItemType.values()) {
            itemTypeMap.put(itemType.value, itemType);
        }
    }

    private final String value;

    ItemType(String value) {
        this.value = value;
    }
}
