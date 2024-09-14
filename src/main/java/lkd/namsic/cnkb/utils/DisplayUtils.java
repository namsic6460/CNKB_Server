package lkd.namsic.cnkb.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DisplayUtils {

    private final static String FILLED_BAR = "█";
    private final static String[] HALF_BAR = {"", "▏", "▎", "▍", "▌", "▋", "▊", "▉", "█"};

    public static String getBar(int value, int maxValue) {
        double percent = 10.0 * value / maxValue;
        double dec = percent % 1;
        int filled = (int) percent;

        StringBuilder output = new StringBuilder("[");
        output.append(FILLED_BAR.repeat(Math.max(0, filled)));
        output.append(HALF_BAR[(int) Math.round(dec * 8)]);
        output.append("   ".repeat(Math.max(0, 9 - filled)));

        if(percent != 10) {
            for(double i = 1; i > percent % 1; i -= 0.3) {
                output.append(" ");
            }
        }

        return output + "] (" + value + "/" + maxValue + ")";
    }
}
