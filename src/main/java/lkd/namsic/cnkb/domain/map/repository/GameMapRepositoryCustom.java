package lkd.namsic.cnkb.domain.map.repository;

import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.MapType;

import java.util.Optional;

public interface GameMapRepositoryCustom {

    GameMap findByMapType(MapType mapType);
    Optional<GameMap> findByLocation(Location location);
}
