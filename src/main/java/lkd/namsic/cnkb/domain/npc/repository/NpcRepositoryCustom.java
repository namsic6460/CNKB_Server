package lkd.namsic.cnkb.domain.npc.repository;

import java.util.Optional;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.enums.domain.NpcType;

public interface NpcRepositoryCustom {

    Npc findByNpcType(NpcType npcType);
    Optional<Npc> findByNpcTypeAndGameMap(NpcType npcType, GameMap map);
}
