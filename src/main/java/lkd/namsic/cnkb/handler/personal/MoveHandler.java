package lkd.namsic.cnkb.handler.personal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.map.repository.GameMapRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.domain.ItemType;
import lkd.namsic.cnkb.enums.domain.MapType;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.InventoryService;
import lkd.namsic.cnkb.utils.LevelUtils;
import lkd.namsic.cnkb.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

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
        this.checkMinLength(commands, 2);
    }

    @Nullable
    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = userData.getUser();
        GameMap gameMap = user.getCurrentGameMap();

        String mapName = commands.stream()
            .skip(1)
            .collect(Collectors.joining(" "));

        if (commands.size() == 3) {
            int targetX;
            int targetY;
            try {
                targetX = Integer.parseInt(commands.get(1));
                targetY = Integer.parseInt(commands.get(2));

                if (targetX < 0 || targetY < 0) {
                    throw new NumberFormatException();
                }

                GameMap targetMap = this.gameMapRepository.findByLocation(new Location(targetX, targetY)).orElseThrow();
                return this.moveToMap(user, gameMap, targetMap);
            } catch (NumberFormatException e) {
                // DO NOTHING
            }
        }

        MapType mapType = MapType.find(mapName);
        GameMap targetGameMap = this.gameMapRepository.findByMapType(mapType);

        return this.moveToMap(user, gameMap, targetGameMap);
    }

    public HandleResult moveToMap(User user, GameMap currentGameMap, GameMap targetGameMap) {
        int gameMapLimitLv = currentGameMap.getLimitLv();
        int userLv = user.getLv();
        if (userLv < gameMapLimitLv) {
            throw new UserReplyException("요구 레벨이 부족합니다\n(레벨: " + userLv + ", 요구 레벨: " + gameMapLimitLv + ")");
        }

        Location targetLocation = targetGameMap.getLocation();
        Location currentLocation = currentGameMap.getLocation();

        double maxMoveDistance = LevelUtils.getMaxMoveDistance(userLv);
        double moveDistance = MathUtils.getDistance(currentLocation, targetLocation);
        if (moveDistance > maxMoveDistance) {
            throw new UserReplyException("이동할 수 없는 거리입니다\n(이동 거리: " + moveDistance + ", 이동 가능 거리: " + maxMoveDistance + ")");
        }

        if (moveDistance == 0) {
            throw new UserReplyException("현재 위치로는 이동할 수 없습니다");
        }

        String innerMessage = null;
        if (!targetGameMap.getMapLimitItems().isEmpty() && !targetGameMap.getPassedUsers().contains(user)) {
            Map<ItemType, Integer> resultItemCountMap = new HashMap<>();
            targetGameMap.getMapLimitItems().stream()
                .filter(limitItem -> {
                    ItemType itemType = limitItem.getKey().getItem().getItemType();
                    int itemCount = this.inventoryService.getItemCount(user, itemType);
                    int useCount = limitItem.getCount();

                    int resultCount = itemCount - useCount;
                    resultItemCountMap.put(itemType, resultCount);

                    return resultCount < 0;
                })
                .findAny()
                .ifPresent(limitItem -> {
                    ItemType itemType = limitItem.getKey().getItem().getItemType();
                    int currentCount = this.inventoryService.getItemCount(user, itemType);

                    throw new UserReplyException("요구 아이템이 부족합니다\n필요 아이템: " + itemType +
                        "(보유 개수: " + currentCount + ", 요구 개수: " + limitItem.getCount() + ")");
                });

            StringBuilder builder = new StringBuilder();
            targetGameMap.getMapLimitItems().forEach((limitItem) -> {
                ItemType itemType = limitItem.getKey().getItem().getItemType();
                Integer resultItemCount = resultItemCountMap.get(itemType);
                this.inventoryService.setItem(user, itemType, resultItemCount);

                builder.append(itemType.getValue())
                    .append(" 아이템을 ")
                    .append(limitItem.getCount())
                    .append("개 소모했습니다\n(남은 개수: ")
                    .append(resultItemCount)
                    .append("개)\n\n");
            });

            targetGameMap.getPassedUsers().add(user);
            innerMessage = builder.toString().trim();
        }

        this.gameMapRepository.save(targetGameMap);
        this.userRepository.updateGameMap(user, targetGameMap);

        return new HandleResult(targetGameMap.getName() + "(" + targetLocation.x() + ", " + targetLocation.y() + ")" + " (으)로 이동했습니다", innerMessage);
    }
}
