package lkd.namsic.cnkb.domain.npc.repository;

import lkd.namsic.cnkb.domain.npc.Npc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NpcRepository extends JpaRepository<Npc, Long>, NpcRepositoryCustom {
}
