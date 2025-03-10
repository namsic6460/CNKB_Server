package lkd.namsic.cnkb.handler.mine;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lkd.namsic.cnkb.common.Emoji;
import lkd.namsic.cnkb.constant.MineConstants;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.domain.ItemType;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlchemyHandler extends AbstractHandler {

    private final SecureRandom random = new SecureRandom();
    private final InventoryService inventoryService;

    @Override
    public List<String> getRootCommands() {
        return List.of("연금술", "연금", "alchemy", "a");
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
        boolean isCheck = Set.of("check", "c", "체크", "확인").contains(commands.get(1));

        int count = 1;
        int toIndex = commands.size();
        try {
            if (!isCheck) {
                count = Integer.parseInt(commands.getLast());
                toIndex--;
            }
        } catch (NumberFormatException e) {
            // DO NOTHING
        }

        boolean isReverse = Set.of("reverse", "r", "역").contains(commands.get(1));
        ItemType itemType = ItemType.find(String.join(" ", commands.subList(isCheck || isReverse ? 2 : 1, toIndex)));

        if (isCheck) {
            int mineralIndex = MineConstants.MINER_MINERAL_ITEMS.indexOf(itemType);
            if (mineralIndex <= 0) {
                throw new UserReplyException("연금술 확률을 확인할 수 없는 아이템입니다");
            }

            double successPercent = MineConstants.getMineralAlchemySuccessPercent(itemType, user.getLv());
            return new HandleResult(Emoji.focus(itemType.getValue()) + " 연금술에 성공할 확률은 " + (successPercent * 100) + "% 입니다");
        }

        StringBuilder builder;
        if (MineConstants.MINER_MINERAL_ITEMS.contains(itemType)) {
            builder = this.convertMineralItems(user, itemType, count, !isReverse);
        } else {
            builder = this.convertOreItems(user, itemType, count);
        }

        return new HandleResult("연금술이 완료되었습니다", builder.toString());
    }

    private StringBuilder convertMineralItems(User user, ItemType resultItemType, int resultCount, boolean isUpgrade) {
        int currentMineralIndex = MineConstants.MINER_MINERAL_ITEMS.indexOf(resultItemType);
        int invalidIndex = isUpgrade ? 0 : MineConstants.MINER_MINERAL_ITEMS.size();
        if (currentMineralIndex == -1 || currentMineralIndex == invalidIndex) {
            throw new UserReplyException("연금술을 진행할 수 없는 아이템입니다");
        }

        ItemType useItemType = MineConstants.MINER_MINERAL_ITEMS.get(currentMineralIndex + (isUpgrade ? -1 : 1));
        int currentCount = this.inventoryService.getItemCount(user, useItemType);
        int useCount = resultCount * (isUpgrade ? 2 : 1);

        if (currentCount < useCount) {
            throw new UserReplyException("연금술에 필요한 아이템이 부족합니다\n"
                + "필요한 아이템: " + useItemType.getValue() + "\n"
                + "현재 보유량: " + currentCount + "개, 필요 보유량: " + useCount);
        }

        int failCount = 0;
        double successPercent = MineConstants.getMineralAlchemySuccessPercent(resultItemType, user.getLv());
        for (int i = 0; i < resultCount; i++) {
            if (isUpgrade && this.random.nextDouble() > successPercent) {
                failCount++;
            }
        }

        this.inventoryService.setItem(user, useItemType, currentCount - useCount);

        StringBuilder builder = this.applyResult(user, Map.of(resultItemType, resultCount - failCount));
        builder.append("\n")
            .append("실패한 개수: ")
            .append(failCount)
            .append("개");

        return builder;
    }

    private StringBuilder convertOreItems(User user, ItemType itemType, int useCount) {
        if (!MineConstants.MINER_ORE_ITEMS.contains(itemType)) {
            throw new UserReplyException("연금술을 진행할 수 없는 아이템입니다");
        }

        int currentCount = this.inventoryService.getItemCount(user, itemType);
        if (currentCount < useCount) {
            throw new UserReplyException("아이템의 보유량 이상으로 연금술을 진행할 수 없습니다\n"
                + "현재 보유량: " + currentCount + "개, 필요 보유량: " + useCount + "개");
        }

        Map<ItemType, Integer> convertedMap = new HashMap<>();
        ItemType convertedItemType = this.getConvertedItem(itemType);
        for (int i = 0; i < useCount; i++) {
            convertedMap.put(convertedItemType, convertedMap.getOrDefault(convertedItemType, 0) + 1);
        }

        this.inventoryService.setItem(user, itemType, currentCount - useCount);

        return this.applyResult(user, convertedMap);
    }

    private ItemType getConvertedItem(ItemType itemType) {
        List<Map.Entry<ItemType, Double>> percents = MineConstants.ALCHEMY_ORE_CONVERSION_PERCENTS.get(itemType);

        double currentValue = 0;
        double randomValue = this.random.nextDouble();
        for (Map.Entry<ItemType, Double> entry : percents) {
            currentValue += entry.getValue();
            if (randomValue > currentValue) {
                continue;
            }

            return entry.getKey();
        }

        log.error("확률이 잘못되었습니다 - {} {}", itemType, randomValue);
        throw new IllegalStateException();
    }

    private StringBuilder applyResult(User user, Map<ItemType, Integer> convertedMap) {
        StringBuilder builder = new StringBuilder("===변환된 아이템===");
        convertedMap.entrySet().stream()
            .sorted(Comparator.comparing(entry -> entry.getKey().getValue()))
            .forEach(entry -> {
                ItemType convertedItemType = entry.getKey();
                Integer convertedCount = entry.getValue();

                this.inventoryService.modifyItemCount(user, convertedItemType, convertedCount);
                builder.append("\n")
                    .append(convertedItemType.getValue())
                    .append(": ")
                    .append(convertedCount)
                    .append("개");
            });

        return builder;
    }
}
