package lkd.namsic.cnkb.handler.personal;

import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.map.repository.GameMapRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.enums.MapType;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.InventoryService;
import lkd.namsic.cnkb.utils.LevelUtils;
import lkd.namsic.cnkb.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MoveHandler extends AbstractHandler {

    private final GameMapRepository gameMapRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    @Override
    public List<String> getRootCommands() {
        return List.of("이동", "move");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        this.checkUser(userData);
    }

    @Nullable
    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = userData.getUser();
        GameMap gameMap = user.getCurrentGameMap();

        switch (commands.size()) {
            case 2 -> {
                UserReplyException unknownMapTypeException = new UserReplyException("존재하지 않는 맵입니다");
                MapType mapType = Optional.of(MapType.valueOf(commands.get(1))).orElseThrow(() -> unknownMapTypeException);
                GameMap targetGameMap = gameMapRepository.findByMapType(mapType);

                return moveToMap(user, gameMap, targetGameMap);
            }
            case 3 -> {
                UserReplyException parseFailException = new UserReplyException("이동하려는 좌표는 음이 아닌 정수여야 합니다");
                UserReplyException emptyMapException = new UserReplyException("해당 좌표엔 맵이 없습니다");

                int targetX = Optional.of(Integer.parseInt(commands.get(1))).orElseThrow(() -> parseFailException);
                int targetY = Optional.of(Integer.parseInt(commands.get(2))).orElseThrow(() -> parseFailException);

                GameMap targetMap = gameMapRepository.findByLocation(new Location(targetX, targetY)).orElseThrow(() -> emptyMapException);

                return moveToMap(user, gameMap, targetMap);
            }
        }

        throw new UserReplyException();
    }

    public HandleResult moveToMap(User user, GameMap currentGameMap, GameMap targetGameMap) {
        int gameMapLimitLv = currentGameMap.getLimitLv();
        int userLv = user.getLv();

        if (userLv < gameMapLimitLv) {
            throw new UserReplyException("요구 레벨이 부족합니다\n(레벨 : " + userLv + ", 요구 레벨 : " + gameMapLimitLv + ")");
        }

        Location targetLocation = targetGameMap.getLocation();
        Location currentLocation = currentGameMap.getLocation();

        double maxMoveDistance = LevelUtils.getMaxMoveDistance(userLv);
        double moveDistance = MathUtils.getDistance(currentLocation, targetLocation);

        if (moveDistance < maxMoveDistance) {
            throw new UserReplyException("이동할 수 없는 거리입니다\n(이동 거리 : " + moveDistance + ", 이동 가능 거리 : " + maxMoveDistance + ")");
        }

        ItemType requiredItemType = targetGameMap.getItemType();

        String itemUseMessage = "";

        if (requiredItemType != null && !targetGameMap.getPassedUsers().contains(user)) {
            if (inventoryService.getItemCount(user, requiredItemType) == 0) {
                throw new UserReplyException("\"" + requiredItemType.getValue() + "\" 아이템이 필요합니다");
            }
            inventoryService.modifyItemCount(user, requiredItemType, -1);
            itemUseMessage = "\"" + requiredItemType.getValue() + "\" 아이템을 하나 소모했습니다\n(남은 개수 : " + inventoryService.getItemCount(user, requiredItemType) + ")\n";
        }
        targetGameMap.addPassedUser(user);
        gameMapRepository.save(targetGameMap);

        userRepository.updateGameMap(user, targetGameMap);

        return new HandleResult(itemUseMessage + "x: " + targetLocation.x() + ", y: " + targetLocation.y() + "(" + targetGameMap.getName() + ")" + "으로 이동했습니다");
    }
}
