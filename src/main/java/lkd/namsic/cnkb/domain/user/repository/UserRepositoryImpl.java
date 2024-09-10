package lkd.namsic.cnkb.domain.user.repository;

import static lkd.namsic.cnkb.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.ActionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByName(String name) {
        return this.queryFactory.selectFrom(user)
            .where(user.name.eq(name))
            .fetchFirst() != null;
    }

    @Override
    public void updateActionType(User targetUser, ActionType actionType) {
        this.queryFactory.update(user)
            .where(user.eq(targetUser))
            .set(user.actionType, actionType)
            .execute();
    }

    @Override
    public void updateChat(User targetUser, Chat chat, boolean isForceChat) {
        this.queryFactory.update(user)
            .where(user.eq(targetUser))
            .set(user.actionType, isForceChat ? ActionType.FORCE_CHAT : ActionType.CHAT)
            .set(user.chat, chat)
            .execute();
    }
}
