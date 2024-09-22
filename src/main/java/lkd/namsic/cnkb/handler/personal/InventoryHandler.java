package lkd.namsic.cnkb.handler.personal;

import lkd.namsic.cnkb.domain.item.dto.InventoryItemDto;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InventoryHandler extends AbstractHandler {

    private final InventoryService inventoryService;

    @Override
    public List<String> getRootCommands() {
        return List.of("인벤토리", "인벤", "inventory", "inven", "inv");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        this.checkUser(userData);

        if (commands.size() > 1) {
            try {
                Integer.parseInt(commands.get(1));
            } catch (NumberFormatException e) {
                throw new UserReplyException("정확한 페이지를 입력해주세요");
            }
        }
    }

    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        int page = 1;
        if (commands.size() > 1) {
            page = Integer.parseInt(commands.get(1));
        }

        User user = userData.getUser();
        int maxPage = this.inventoryService.getItemCount(user) / 50;
        if (page - 1 > maxPage) {
            throw new UserReplyException("최대 " + page + " 까지 표시할 수 있습니다");
        }

        List<ItemType> priorityItemTypes = user.getPriorityItemTypes();
        priorityItemTypes.sort(Comparator.comparing(ItemType::getValue));

        InventoryItemDto.InventoryItemDtoListWithPriority items = this.inventoryService
            .getItems(user, page, priorityItemTypes);

        Map<ItemType, InventoryItemDto> priorityDtoMap = items.priorityList().stream()
            .collect(Collectors.toMap(InventoryItemDto::itemType, Function.identity()));
        List<InventoryItemDto> dtoList = items.dtoList();

        // 1페이지 / 1페이지)\n인벤토리가 비어있습니다
        StringBuilder messageBuilder = new StringBuilder("\n인벤토리 (")
            .append(page)
            .append("페이지 / ")
            .append(maxPage)
            .append("페이지)");

        if (dtoList.isEmpty()) {
            messageBuilder.append("\n인벤토리가 비어있습니다");
            return new HandleResult(messageBuilder.toString());
        }

        if (priorityItemTypes.isEmpty()) {
            messageBuilder.append("\n우선 표시 설정된 아이템이 없습니다");
        } else {
            priorityItemTypes.forEach(itemType -> {
                int itemCount = Optional.ofNullable(priorityDtoMap.get(itemType))
                    .map(InventoryItemDto::count)
                    .orElse(0);

                messageBuilder.append("\n")
                    .append(itemType.getValue())
                    .append(": ")
                    .append(itemCount)
                    .append("개");
            });
        }

        String innerMessage = dtoList.stream()
            .map(dto -> dto.itemType().getValue() + ": " + dto.count() + "개")
            .collect(Collectors.joining("\n"));

        return new HandleResult(messageBuilder.toString(), innerMessage);
    }
}
