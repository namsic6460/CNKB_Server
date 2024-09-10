package lkd.namsic.cnkb.domain.npc.repository;

import static lkd.namsic.cnkb.domain.npc.QNpc.npc;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.enums.NpcType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NpcRepositoryImpl implements NpcRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Npc findByType(NpcType npcType) {
        return this.queryFactory.selectFrom(npc)
            .where(npc.name.eq(npcType.getValue()))
            .fetchFirst();
    }
}
