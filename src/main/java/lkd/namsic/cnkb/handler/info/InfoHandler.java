package lkd.namsic.cnkb.handler.info;

import lkd.namsic.cnkb.domain.item.Item;
import lkd.namsic.cnkb.domain.item.repository.ItemRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.utils.DisplayUtils;
import lkd.namsic.cnkb.utils.LevelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InfoHandler extends AbstractHandler {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<String> getRootCommands() {
        return List.of("정보", "info", "i");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        this.checkUser(userData);
    }

    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        int length = commands.size();

        switch (length) {
            case 1 -> {
                return this.getUserInfo(Objects.requireNonNull(userData.user()));
            }

            case 2 -> {
                String subCommand = commands.get(1);
                switch (subCommand.toLowerCase()) {
                    case "아이템", "item" -> throw new UserReplyException("아이템의 이름을 입력해주세요");
                    case "장비", "equip" -> throw new UserReplyException("장비의 이름을 입력해주세요");
                    case "몬스터", "monster" -> throw new UserReplyException("몬스터의 이름을 입력해주세요");
                    case "스킬", "skill" -> throw new UserReplyException("스킬의 이름을 입력해주세요");
                    case "퀘스트", "quest" -> throw new UserReplyException("퀘스트의 이름을 입력해주세요");

                    default -> {
                        User user = this.userRepository.findByName(subCommand)
                            .orElseThrow(() -> new UserReplyException("유저를 찾을 수 없습니다"));
                        return this.getUserInfo(user);
                    }
                }
            }

            default -> {
                String subCommand = commands.get(1);
                String target = String.join(" ", commands.subList(2, commands.size())).trim();

                switch (subCommand.toLowerCase()) {
                    case "아이템", "item" -> {
                        ItemType itemType = ItemType.itemTypeMap.get(target);
                        if (itemType == null) {
                            throw new UserReplyException("아이템을 찾을 수 없습니다");
                        }

                        Item item = this.itemRepository.findByItemType(itemType);
                        return this.getItemInfo(item);
                    }

                    default -> throw new UserReplyException();
//                    case "장비", "equip" -> throw new UserReplyException("장비의 이름을 입력해주세요");
//                    case "몬스터", "monster" -> throw new UserReplyException("몬스터의 이름을 입력해주세요");
//                    case "스킬", "skill" -> throw new UserReplyException("스킬의 이름을 입력해주세요");
//                    case "퀘스트", "quest" -> throw new UserReplyException("퀘스트의 이름을 입력해주세요");
                }
            }
        }
    }

    private HandleResult getUserInfo(User user) {
        return new HandleResult(
            "===" + user.getName() + "님의 정보===\n" +
                "\uD83D\uDCB0 소지금: " + user.getMoney() + "G\n" +
                "♥ 체력: " + DisplayUtils.getBar(user.getHp(), user.getMaxHp()) + "\n" +
                "\uD83D\uDCA7 마나: " + DisplayUtils.getBar(user.getMn(), user.getMaxMn()) + "\n" +
                "⭐ 레벨: " + user.getLv() + "Lv (" + user.getExp() + "/" + LevelUtils.getRequiredExp(user.getLv()) + ")\n" +
                "\uD83C\uDF59 스텟 포인트: " + user.getSp(),
            "제작중..."
        );
    }

    private HandleResult getItemInfo(Item item) {
        return new HandleResult(
            "<" + item.getItemType().getValue() + ">\n" + item.getDescription(),
            "[획득 방법]\n" + item.getAcquireWay()
        );
    }
}
