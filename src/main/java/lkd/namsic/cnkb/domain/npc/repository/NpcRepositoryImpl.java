package lkd.namsic.cnkb.domain.npc.repository;

import static lkd.namsic.cnkb.domain.npc.QNpc.npc;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.enums.domain.NpcType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NpcRepositoryImpl implements NpcRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Npc findByNpcType(NpcType npcType) {
        return this.queryFactory.selectFrom(npc)
            .where(npc.npcType.eq(npcType))
            .fetchFirst();
    }

    @Override
    public Optional<Npc> findByNpcTypeAndGameMap(NpcType npcType, GameMap map) {
        return Optional.ofNullable(
            this.queryFactory.selectFrom(npc)
                .where(npc.npcType.eq(npcType), npc.gameMap.eq(map))
                .fetchFirst()
        );
    }
}
