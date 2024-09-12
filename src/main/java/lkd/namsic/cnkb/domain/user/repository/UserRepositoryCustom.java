package lkd.namsic.cnkb.domain.user.repository;

import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.ActionType;

public interface UserRepositoryCustom {

    boolean existsByName(String name);
    void updateActionType(User targetUser, ActionType actionType);
    void updateChat(User targetUser, Chat chat);
    ActionType getActionType(User targetUser);
    void clearChat(User targetUser);
}
