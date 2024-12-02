package lkd.namsic.cnkb.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.floor(100 * Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) / 100;
    }
}
