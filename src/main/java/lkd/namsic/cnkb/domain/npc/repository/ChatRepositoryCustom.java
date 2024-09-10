package lkd.namsic.cnkb.domain.npc.repository;

import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.enums.NamedChat;

public interface ChatRepositoryCustom {

    Chat findByNamedChat(NamedChat namedChat);
    Chat findByIdWithNpc(Long nextChatId);
}
