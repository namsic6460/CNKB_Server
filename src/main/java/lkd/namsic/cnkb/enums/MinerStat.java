package lkd.namsic.cnkb.enums;

import lkd.namsic.cnkb.domain.user.Miner;
import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Getter
public enum MinerStat {

    SPEED(List.of("속도", "speed"), 10, lv -> (long) Math.floor(Math.log10(lv) * 100)),
    QUALITY(List.of("등급", "quality"), 15, lv -> (long) Math.pow(lv, 2) * 100),
    STORAGE(List.of("저장량", "storage"), 20, lv -> lv * 2L),
    ;

    private static final HashMap<String, MinerStat> minerStatMap = new HashMap<String, MinerStat>();

    static {
        for (MinerStat minerStat : MinerStat.values()) {
            minerStat.keywords.forEach(value -> minerStatMap.put(value, minerStat));
        }
    }

    private final List<String> keywords;
    private final int maxLv;
    private final Function<Integer, Long> requiredMoneyForNextLv;

    MinerStat(List<String> keywords, int maxLv, Function<Integer, Long> requiredMoneyForNextLv) {
        this.keywords = keywords;
        this.maxLv = maxLv;
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
        switch (minerStat) {
            case SPEED -> {
                return miner.getSpeedLv();
            }
            case QUALITY -> {
                return miner.getQualityLv();
            }
            case STORAGE -> {
                return miner.getStorageLv();
            }
            default -> throw DataNotFoundException.minerStat();
        }
    }

    public boolean isMaxLv(int currentLv) {
        return this.getMaxLv() == currentLv;
    }

    public long getRequiredMoneyForNextLv(int currentLv) {
        return this.requiredMoneyForNextLv.apply(currentLv);
    }
}
