package lkd.namsic.cnkb.constant;

import java.util.List;
import java.util.Map;
import lkd.namsic.cnkb.enums.domain.ItemType;

public class MineConstants {

    public static final List<Integer> MINER_GATHER_DELAY = List.of(30, 25, 20, 15, 10);
    public static final List<Integer> MINER_MAX_STORAGE_COUNT = List.of(500, 2000, 5000, 25000, 100000);
    public static final List<ItemType> MINER_ORE_ITEMS = List.of(
        ItemType.LOWEST_ORE_STONE,
        ItemType.LOW_ORE_STONE,
        ItemType.MIDDLE_ORE_STONE,
        ItemType.HIGH_ORE_STONE,
        ItemType.HIGHEST_ORE_STONE
    );

    public static final List<ItemType> MINER_MINERAL_ITEMS = List.of(
        ItemType.STONE,
        ItemType.COAL,
        ItemType.COPPER,
        ItemType.TIN,
        ItemType.ZINC,
        ItemType.LEAD,
        ItemType.IRON,
        ItemType.SILVER,
        ItemType.GOLD,
        ItemType.GLOW_STONE,
        ItemType.RED_IRON,
        ItemType.BLACK_IRON,
        ItemType.ANTHRACITE,
        ItemType.TITANIUM
    );

    public static final Map<ItemType, List<Map.Entry<ItemType, Double>>> ALCHEMY_ORE_CONVERSION_PERCENTS = Map.of(
        ItemType.LOWEST_ORE_STONE, List.of(
            Map.entry(ItemType.SAND, 0.2),
            Map.entry(ItemType.STONE, 0.5),
            Map.entry(ItemType.COAL, 0.3)
        ),
        ItemType.LOW_ORE_STONE, List.of(
            Map.entry(ItemType.STONE, 0.35),
            Map.entry(ItemType.COAL, 0.25),
            Map.entry(ItemType.COPPER, 0.2),
            Map.entry(ItemType.TIN, 0.04),
            Map.entry(ItemType.ZINC, 0.08),
            Map.entry(ItemType.LEAD, 0.08)
        ),
        ItemType.MIDDLE_ORE_STONE, List.of(
            Map.entry(ItemType.STONE, 0.35),
            Map.entry(ItemType.IRON, 0.35),
            Map.entry(ItemType.SILVER, 0.35),
            Map.entry(ItemType.GOLD, 0.35),
            Map.entry(ItemType.GLOW_STONE, 0.35),
            Map.entry(ItemType.RED_IRON, 0.35),
            Map.entry(ItemType.BLACK_IRON, 0.35),
            Map.entry(ItemType.ANTHRACITE, 0.35),
            Map.entry(ItemType.TITANIUM, 0.35)
        )
    );

    public static double getMineralAlchemySuccessPercent(ItemType itemType, int lv) {
        double mineralUpgradePercent = switch (itemType) {
            case COAL -> 0.2 + lv * 0.01;
            case COPPER, TIN, ZINC, LEAD -> 0.1 + lv * 0.005;
            case IRON -> (lv - 30) / 140d;
            case SILVER -> (lv - 50) / 200d;
            case GOLD -> (lv - 100) / 800d;
            case GLOW_STONE, RED_IRON, BLACK_IRON -> (lv - 50) / 300d;
            case ANTHRACITE, TITANIUM -> (lv - 100) / 400d;
            default -> 0.5;
        };

        return Math.round(Math.min(0.5, Math.max(0, mineralUpgradePercent)) * 1000) / 1000d;
    }
}
