package lkd.namsic.cnkb.enums.domain;

import java.util.HashMap;
import java.util.Map;
import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

@Getter
public enum NpcType {

    SYSTEM("시스템"),
    NOA("노아"),
    ;

    private static final Map<String, NpcType> npcTypeMap = new HashMap<>();

    public static NpcType find(String npcName) {
        NpcType npcType = npcTypeMap.get(npcName);
        if (npcType == null) {
            throw DataNotFoundException.npc();
        }

        return npcType;
    }

    static {
        for (NpcType npcType : NpcType.values()) {
            npcTypeMap.put(npcType.value, npcType);
        }
    }

    private final String value;

    NpcType(String value) {
        this.value = value;
    }
}
