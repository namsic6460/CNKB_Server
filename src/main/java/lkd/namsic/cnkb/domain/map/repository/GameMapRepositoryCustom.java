package lkd.namsic.cnkb.domain.map.repository;

import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.enums.MapType;

public interface GameMapRepositoryCustom {

    GameMap findByMapType(MapType mapType);
}
