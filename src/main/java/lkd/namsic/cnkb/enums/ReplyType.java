package lkd.namsic.cnkb.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;

public enum ReplyType {

    YES("네", "예", "응", "ㅇㅇ", "예스", "yes", "y"),
    NO("아니", "ㄴㄴ", "no", "n"),
    ONE("1"),
    TWO("2")
    ;

    private final Set<String> values;

    ReplyType(String...values) {
        this.values = Set.of(values);
    }

    public static ReplyType parse(String string) {
        if (StringUtils.isBlank(string)) {
            return null;
        }

        return Arrays.stream(ReplyType.values())
            .filter(replyType -> replyType.values.contains(string.toLowerCase()))
            .findAny()
            .orElse(null);
    }
}
