package lkd.namsic.cnkb.enums;

import lkd.namsic.cnkb.domain.user.Miner;
import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@Getter
public enum MinerStat {

    SPEED(List.of("속도", "speed"), 10),
    QUALITY(List.of("등급", "quality"), 15),
    STORAGE(List.of("저장량", "storage"), 20),
    ;

    private static final HashMap<String, MinerStat> minerStatMap = new HashMap<String, MinerStat>();

    private final List<String> value;
    private final int maxLv;

    public boolean isMaxLv(int currentLv) {
        return this.getMaxLv() == currentLv;
    }

    public static long getRequiredMoney(MinerStat minerStat, long currentStatValue) {
        switch (minerStat) {
            case SPEED -> {
                return (long) Math.floor(Math.log10(currentStatValue) * 100);
            }

            case QUALITY -> {
                return (long) Math.pow(currentStatValue, 2) * 100;
            }

            case STORAGE -> {
                return currentStatValue * 2;
            }

            default -> throw DataNotFoundException.minerStat();
        }
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

    static {
        for (MinerStat minerStat : MinerStat.values()) {
            minerStat.value.forEach(value -> minerStatMap.put(value, minerStat));
        }
    }

    MinerStat(List<String> value, int maxLv) {
        this.value = value;
        this.maxLv = maxLv;
    }
}
