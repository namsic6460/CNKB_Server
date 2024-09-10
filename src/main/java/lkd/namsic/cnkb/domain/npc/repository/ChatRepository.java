package lkd.namsic.cnkb.domain.npc.repository;

import lkd.namsic.cnkb.domain.npc.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
}
