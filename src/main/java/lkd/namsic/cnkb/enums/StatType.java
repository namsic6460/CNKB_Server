package lkd.namsic.cnkb.enums;

import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum StatType {

    ATK("공격력"),
    MATK("마법 공격력"),
    AS("공격 속도"),
    DEF("방어력"),
    MDEF("마법 방어력"),
    DRA("흡수력"),
    MDRA("마법 흡수력"),
    BRE("관통력"),
    MBRE("마법 관통력"),
    CRIP("치명타 확률"),
    CRID("치명타 데미지"),
    ;

    public static final Map<StatType, Long> DEFAULT_STAT = new EnumMap<>(Map.ofEntries(
        Map.entry(StatType.ATK, 50L),
        Map.entry(StatType.MATK, 10L),
        Map.entry(StatType.AS, 100L),
        Map.entry(StatType.DEF, 0L),
        Map.entry(StatType.MDEF, 0L),
        Map.entry(StatType.DRA, 0L),
        Map.entry(StatType.MDRA, 0L),
        Map.entry(StatType.BRE, 0L),
        Map.entry(StatType.MBRE, 0L),
        Map.entry(StatType.CRIP, 0L),
        Map.entry(StatType.CRID, 0L)
    ));

    private final String value;

    StatType(String value) {
        this.value = value;
    }
}
