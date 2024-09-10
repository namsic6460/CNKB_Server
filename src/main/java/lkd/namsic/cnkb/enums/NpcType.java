package lkd.namsic.cnkb.enums;

import lombok.Getter;

@Getter
public enum NpcType {

    UNKNOWN("???"),
    ;

    private final String value;

    NpcType(String value) {
        this.value = value;
    }
}
