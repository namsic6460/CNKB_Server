package lkd.namsic.cnkb.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LevelUtils {

    public static long getRequiredExp(int lv) {
        long needExp;
        if(lv <= 100) {
            needExp = 10_000 + 5_000L * lv;
        } else if(lv <= 300) {
            needExp = -200_000 + (long) (Math.pow(lv - 10, 2.5) * 2) + 10_000 * lv;
        } else if(lv <= 500) {
            needExp = -50_000_000 + 200_000 * lv;
        } else if(lv <= 750) {
            needExp = -50_000_000 + (long) (Math.pow(lv, 3.1) / 1.5);
        } else if(lv <= 950) {
            needExp = 45_000_000L * (lv - 750) + 1_000_000_000L;
        } else {
            needExp = -30_000_000_000L + (long) Math.pow(lv - 600, 4.2);
        }

        return needExp;
    }

    public static double getMaxMoveDistance(int lv) {
        int intDistance = (lv - 1) / 25;
        return 1 + intDistance;
    }

    public long getKillExp(int lv, int enemyLv) {
        long exp = 5000 + (long) ((getRequiredExp(enemyLv) * 0.00001) + (getRequiredExp(lv) * 0.00001));

        int gap = lv - enemyLv;
        if(Math.abs(gap) > 10 && lv > 10) {
            long sqrtValue = (long) Math.sqrt(Math.sqrt(lv - 9) / 1.2);

            if(gap > 0) {
                exp /= sqrtValue;
            } else {
                exp *= sqrtValue;
            }
        }

        return (int) ((1 + 0.125 * enemyLv) * exp);
    }
}
