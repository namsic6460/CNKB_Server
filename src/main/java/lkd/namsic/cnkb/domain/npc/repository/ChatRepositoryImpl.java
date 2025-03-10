package lkd.namsic.cnkb.domain.npc.repository;

import static lkd.namsic.cnkb.domain.npc.QChat.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.enums.NamedChat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Chat findByNamedChat(NamedChat namedChat) {
        return this.queryFactory.selectFrom(chat)
            .where(chat.namedChat.eq(namedChat))
            .fetchFirst();
    }

    @Override
    public Chat findByIdWithNpc(Long nextChatId) {
        return this.queryFactory.selectFrom(chat)
            .where(chat.id.eq(nextChatId))
            .leftJoin(chat.npc).fetchJoin()
            .fetchFirst();
    }

    @Override
    public Chat findFirstChat(Npc npc) {
        return this.queryFactory.selectFrom(chat)
            .where(chat.npc.eq(npc), chat.isFirst.isTrue())
            .leftJoin(chat.npc).fetchJoin()
            .fetchFirst();
    }

    @Override
    public List<Chat> findNotConnectedChats(Npc npc) {
        return this.queryFactory.selectFrom(chat)
            .where(chat.npc.eq(npc), chat.isConnected.isFalse())
            .leftJoin(chat.npc).fetchJoin()
            .fetch();
    }
}
