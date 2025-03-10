package lkd.namsic.cnkb.domain.map.repository;

import static lkd.namsic.cnkb.domain.map.QGameMap.gameMap;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.domain.MapType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameMapRepositoryImpl implements GameMapRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public GameMap findByMapType(MapType mapType) {
        return this.queryFactory.selectFrom(gameMap)
            .where(gameMap.name.eq(mapType.getValue()))
            .fetchFirst();
    }

    @Override
    public Optional<GameMap> findByLocation(Location location) {
        return Optional.ofNullable(
            this.queryFactory.selectFrom(gameMap)
                .where(gameMap.location.eq(location))
                .fetchFirst()
        );
    }
}