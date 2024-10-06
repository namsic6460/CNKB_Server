package lkd.namsic.cnkb.enums;

import lombok.Getter;

@Getter
public enum NpcType {

    SYSTEM("시스템"),
    NOA("노아"),
    ;

    private final String value;

    NpcType(String value) {
        this.value = value;
    }
}
