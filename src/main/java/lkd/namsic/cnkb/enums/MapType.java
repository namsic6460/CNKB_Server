package lkd.namsic.cnkb.enums;

import java.util.HashMap;
import lkd.namsic.cnkb.exception.DataNotFoundException;
import lombok.Getter;

@Getter
public enum MapType {

    NONE("NONE"),
    START_VILLAGE("시작의 마을"),
    ADVENTURE_FIELD("모험의 평원"),
    QUITE_SEASHORE("조용한 바닷가"),
    PEACEFUL_RIVER("평화로운 강")
    ;

    private static final HashMap<String, MapType> mapTypeMap = new HashMap<>();

    static {
        for (MapType mapType : MapType.values()) {
            mapTypeMap.put(mapType.value, mapType);
        }
    }

    private final String value;

    MapType(String value) {
        this.value = value;
    }

    public static MapType find(String mapName) {
        MapType mapType = mapTypeMap.get(mapName);
        if (mapType == null) {
            throw DataNotFoundException.minerStat();
        }

        return mapType;
    }
}
