package lkd.namsic.cnkb.domain.npc.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.enums.NamedChat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static lkd.namsic.cnkb.domain.npc.QChat.chat;

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
}
