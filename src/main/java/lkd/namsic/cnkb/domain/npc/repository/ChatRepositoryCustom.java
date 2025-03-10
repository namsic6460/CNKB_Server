package lkd.namsic.cnkb.domain.npc.repository;

import java.util.List;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.enums.NamedChat;

public interface ChatRepositoryCustom {

    Chat findByNamedChat(NamedChat namedChat);
    Chat findByIdWithNpc(Long nextChatId);
    Chat findFirstChat(Npc npc);
    List<Chat> findNotConnectedChats(Npc npc);
}
