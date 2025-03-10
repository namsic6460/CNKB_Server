package lkd.namsic.cnkb.enums;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import lkd.namsic.cnkb.domain.user.Miner;
import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

@Getter
public enum MinerStat {

    SPEED(List.of("속도", "speed"), lv -> switch (lv) {
        case 1 -> 3_000L;
        case 2 -> 50_000L;
        case 3 -> 800_000L;
        case 4 -> 25_000_000L;
        default -> throw new IllegalArgumentException();
    }),
    QUALITY(List.of("등급", "quality"), lv -> switch (lv) {
        case 1 -> 2_000L;
        case 2 -> 30_000L;
        case 3 -> 750_000L;
        case 4 -> 50_000_000L;
        default -> throw new IllegalArgumentException();
    }),
    STORAGE(List.of("저장량", "storage"), lv -> switch (lv) {
        case 1 -> 1000L;
        case 2 -> 10000L;
        case 3 -> 300_000L;
        case 4 -> 15_000_000L;
        default -> throw new IllegalArgumentException();
    }),
    ;

    private static final HashMap<String, MinerStat> minerStatMap = new HashMap<>();

    static {
        for (MinerStat minerStat : MinerStat.values()) {
            minerStat.keywords.forEach(value -> minerStatMap.put(value, minerStat));
        }
    }

    private final List<String> keywords;
    private final Function<Integer, Long> requiredMoneyForNextLv;

    MinerStat(List<String> keywords, Function<Integer, Long> requiredMoneyForNextLv) {
        this.keywords = keywords;
        this.requiredMoneyForNextLv = requiredMoneyForNextLv;
    }

    public static MinerStat find(String minerStatName) {
        MinerStat minerStat = minerStatMap.get(minerStatName);
        if (minerStat == null) {
            throw DataNotFoundException.minerStat();
        }

        return minerStat;
    }

    public static int getMinerStatValue(Miner miner, MinerStat minerStat) {
        return switch (minerStat) {
            case SPEED -> miner.getSpeedLv();
            case QUALITY -> miner.getQualityLv();
            case STORAGE -> miner.getStorageLv();
        };
    }

    public boolean isMaxLv(int currentLv) {
        return currentLv == 5;
    }

    public long getRequiredMoneyForNextLv(int currentLv) {
        return this.requiredMoneyForNextLv.apply(currentLv);
    }
}
