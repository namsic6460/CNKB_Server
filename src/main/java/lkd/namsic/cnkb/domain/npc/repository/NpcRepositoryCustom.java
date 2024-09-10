package lkd.namsic.cnkb.domain.npc.repository;

import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.enums.NpcType;

public interface NpcRepositoryCustom {

    Npc findByType(NpcType npcType);
}
