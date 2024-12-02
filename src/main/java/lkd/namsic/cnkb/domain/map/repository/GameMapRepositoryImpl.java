package lkd.namsic.cnkb.domain.map.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.enums.MapType;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static lkd.namsic.cnkb.domain.map.QGameMap.gameMap;

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
    public Optional<GameMap> findByXY(int x, int y) {
        return Optional.ofNullable(
            this.queryFactory.selectFrom(gameMap)
                .where(gameMap.x.eq(x), gameMap.y.eq(y))
                .fetchFirst()
        );
    }
}
