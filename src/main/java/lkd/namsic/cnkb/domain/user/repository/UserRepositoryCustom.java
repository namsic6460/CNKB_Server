package lkd.namsic.cnkb.domain.user.repository;

import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.ActionType;

import java.util.Optional;

public interface UserRepositoryCustom {

    boolean existsByName(String name);
    void updateActionType(User targetUser, ActionType actionType);
    void updateChat(User targetUser, Chat chat);
    void updateGameMap(User targetUser, GameMap gameMap);
    ActionType getActionType(User targetUser);
    void clearChat(User targetUser);
    void joinAll(User targetUser);

    Optional<User> findByName(String name);
}
