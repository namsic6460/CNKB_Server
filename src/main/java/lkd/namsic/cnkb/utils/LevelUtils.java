package lkd.namsic.cnkb.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LevelUtils {

    public static long getRequiredExp(int lv) {
        return 1000L * lv;
    }

    public static double getMaxMoveDistance(int lv) {
        int intDistance = (lv - 1) / 25;
        return 0.99 + intDistance;
    }
}
