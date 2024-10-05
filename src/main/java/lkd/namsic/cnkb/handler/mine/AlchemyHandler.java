package lkd.namsic.cnkb.handler.mine;

import lkd.namsic.cnkb.constant.MineConstants;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.exception.ReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this.checkLength(commands, 2);
    }

    @Nullable
    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = userData.getUser();

        // TODO

        int count;
        int toIndex = commands.size();
        try {
            count = Integer.parseInt(commands.getLast());
            toIndex--;
        } catch (NumberFormatException e) {
            count = 1;
        }

        ItemType itemType = ItemType.find(String.join(" ", commands.subList(1, toIndex)));
        return this.convertItems(user, itemType, count);
    }

    private HandleResult convertItems(User user, ItemType itemType, int useCount) {
        Integer currentCount = this.inventoryService.getItemCount(user, itemType);
        if (currentCount < useCount) {
            throw new ReplyException("아이템의 보유량 이상으로 연금술을 진행할 수 없습니다\n현재 보유량: " + currentCount + "개");
        }


        Map<ItemType, Integer> convertedMap = new HashMap<>();
        for (int i = 0; i < useCount; i++) {
            ItemType convertedItemType = this.getConvertedItem(itemType);
            convertedMap.put(convertedItemType, convertedMap.getOrDefault(convertedItemType, 0) + 1);
        }

        this.inventoryService.setItem(user, itemType, currentCount - useCount);

        StringBuilder builder = new StringBuilder("===변환된 아이템===");
        convertedMap.entrySet().stream()
            .sorted(Comparator.comparing(entry -> entry.getKey().getValue()))
            .forEach(entry -> {
                ItemType convertedItemType = entry.getKey();
                Integer convertedCount = entry.getValue();

                this.inventoryService.addItem(user, convertedItemType, convertedCount);
                builder.append("\n")
                    .append(convertedItemType.getValue())
                    .append(": ")
                    .append(convertedCount)
                    .append("개");
            });

        return new HandleResult("광석의 연금술이 완료되었습니다", builder.toString());
    }

    private ItemType getConvertedItem(ItemType itemType) {
        double randomValue = this.random.nextDouble();
        List<Map.Entry<ItemType, Double>> percents = MineConstants.ALCHEMY_PERCENTS.get(itemType);

        double currentValue = 0;
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
}
