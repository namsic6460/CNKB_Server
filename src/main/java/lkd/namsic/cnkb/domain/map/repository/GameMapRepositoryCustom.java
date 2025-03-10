package lkd.namsic.cnkb.domain.map.repository;

import java.util.Optional;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.domain.MapType;

public interface GameMapRepositoryCustom {

    GameMap findByMapType(MapType mapType);
    Optional<GameMap> findByLocation(Location location);
}
