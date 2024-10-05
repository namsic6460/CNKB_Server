package lkd.namsic.cnkb.constant;

import lkd.namsic.cnkb.enums.ItemType;

import java.util.List;
import java.util.Map;

public class MineConstants {

    public static final List<Integer> MINER_GATHER_DELAY = List.of(30, 25, 20, 15, 10);

    public static final List<ItemType> MINER_ITEMS = List.of(
        ItemType.LOWEST_ORE_STONE,
        ItemType.LOW_ORE_STONE,
        ItemType.MIDDLE_ORE_STONE
    );

    public static final List<Integer> MINER_MAX_STORAGE_COUNT = List.of(2, 2000, 3000, 5000, 10000);

    public static final Map<ItemType, List<Map.Entry<ItemType, Double>>> ALCHEMY_PERCENTS = Map.of(
        ItemType.LOWEST_ORE_STONE, List.of(
            Map.entry(ItemType.SAND, 0.2),
            Map.entry(ItemType.STONE, 0.5),
            Map.entry(ItemType.COAL, 0.3)
        )
    );
}
