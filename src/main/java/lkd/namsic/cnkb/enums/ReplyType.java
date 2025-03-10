package lkd.namsic.cnkb.enums;

import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum ReplyType {

    YES("네", "예", "응", "ㅇㅇ", "예스", "yes", "y"),
    NO("아니요", "아니오", "아니", "ㄴㄴ", "no", "n"),
    ONE("1"),
    TWO("2")
    ;

    private final String primaryValue;
    private final Set<String> values;

    ReplyType(String...values) {
        this.primaryValue = values[0];
        this.values = Set.of(values);
    }

    @Nullable
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
