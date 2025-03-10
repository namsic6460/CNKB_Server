package lkd.namsic.cnkb.enums;

import java.util.HashMap;
import java.util.Map;
import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

@Getter
public enum ItemType {

    LOWEST_ORE_STONE("최하급 광석"),
    LOW_ORE_STONE("하급 광석"),
    MIDDLE_ORE_STONE("중급 광석"),
    HIGH_ORE_STONE("상급 광석"),
    HIGHEST_ORE_STONE("최상급 광석"),
    SAND("모래"),
    STONE("돌"),
    COAL("석탄"),
    COPPER("구리"),
    TIN("주석"),
    ZINC("아연"),
    LEAD("납"),
    IRON("철"),
    SILVER("은"),
    GOLD("금"),
    GLOW_STONE("발광 석영"),
    RED_IRON("적철"),
    BLACK_IRON("흑철"),
    ANTHRACITE("무연탄"),
    TITANIUM("티타늄")
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
