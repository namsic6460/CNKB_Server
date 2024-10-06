package lkd.namsic.cnkb.enums;

import lombok.Getter;

@Getter
public enum MapType {

    NONE("NONE"),
    START_VILLAGE("시작의 마을"),
    ADVENTURE_FIELD("모험의 평원"),
    QUITE_SEASHORE("조용한 바닷가"),
    PEACEFUL_RIVER("평화로운 강")
    ;

    private final String value;

    MapType(String value) {
        this.value = value;
    }
}
