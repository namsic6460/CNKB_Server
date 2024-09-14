package lkd.namsic.cnkb.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LevelUtils {

    public static long getRequiredExp(int lv) {
        return 1000L * lv;
    }
}
