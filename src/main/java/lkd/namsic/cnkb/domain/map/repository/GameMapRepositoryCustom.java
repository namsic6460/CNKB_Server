package lkd.namsic.cnkb.domain.map.repository;

import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.enums.MapType;

import java.util.Optional;

public interface GameMapRepositoryCustom {

    GameMap findByMapType(MapType mapType);
    Optional<GameMap> findByXY(int x, int y);
}
