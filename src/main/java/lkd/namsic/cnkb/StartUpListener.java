package lkd.namsic.cnkb;

import static lkd.namsic.cnkb.common.Emoji.focus;

import java.util.function.Function;
import lkd.namsic.cnkb.domain.item.Item;
import lkd.namsic.cnkb.domain.item.repository.ItemRepository;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.map.repository.GameMapRepository;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.domain.npc.repository.ChatRepository;
import lkd.namsic.cnkb.domain.npc.repository.NpcRepository;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.enums.MapType;
import lkd.namsic.cnkb.enums.NamedChat;
import lkd.namsic.cnkb.enums.NpcType;
import lkd.namsic.cnkb.enums.ReplyType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class StartUpListener implements ApplicationListener<ApplicationReadyEvent> {

    private final GameMapRepository gameMapRepository;
    private final ItemRepository itemRepository;
    private final NpcRepository npcRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.createGameMaps();
        this.createItems();
        this.createNpcs();
        this.createChats();
    }

    private void createGameMaps() {
        this.gameMapRepository.save(GameMap.create(MapType.NONE, new Location(Integer.MAX_VALUE, Integer.MAX_VALUE), 0));
        this.gameMapRepository.save(GameMap.create(MapType.START_VILLAGE, new Location(0, 0), 0));
        this.gameMapRepository.save(GameMap.create(MapType.ADVENTURE_FIELD, new Location(0, 1), 0));
        this.gameMapRepository.save(GameMap.create(MapType.QUITE_SEASHORE, new Location(1, 0), 0));
        this.gameMapRepository.save(GameMap.create(MapType.PEACEFUL_RIVER, new Location(1, 1), 0));
    }

    private void createItems() {
        this.createItem(
            ItemType.LOWEST_ORE_STONE,
            """
            가장 기본적인 광석이다
            매우 흔하게 발견할 수 있으며, 일반적인 재료만을 얻을 수 있다
            """,
            """
            - 채굴기를 통해 얻을 수 있다
            - 상위 등급의 광석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.LOW_ORE_STONE,
            """
            일반적인 광석이다
            흔하게 발견할 수 있으며, 일반적인 광물을 얻을 수 있다
            """,
            """
            - 2레벨 이상의 채굴기를 통해 얻을 수 있다
            - 상위 등급의 광석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.MIDDLE_ORE_STONE,
            """
            일반적인 채굴기로는 발견할 수 없는 좋은 품질의 광석이다
            """,
            """
            - 3레벨 이상의 채굴기를 통해 얻을 수 있다
            - 상위 등급의 광석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.SAND,
            """
            흔한 모래다
            유리를 만드는 데에 주로 쓰인다
            """,
            """
            - 최하급 광석 또는 하급 광석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.STONE,
            """
            흔한 돌이다
            석재 가공품에 주로 쓰이며, 광물의 대체재로써 일부 활용이 가능하다
            """,
            """
            - 모든 종류의 광석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.COAL,
            """
            연료로 사용할 수 있는 흔한 광물이다
            유연탄 종류들을 모두 포괄하며, 연료의 효율이 좋지는 않다
            """,
            """
            - 최하급 광석 또는 하급 광석에 연금술을 진행하여 얻을 수 있다
            - 돌로 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.COPPER,
            """
            매우 다양한 곳에서 사용되며, 단독 혹은 합금으로써 활용되는 흔한 광물이다
            부정한 기운을 없애는 효과가 있다
            """,
            """
            - 하급 광석에 연금술을 진행하여 얻을 수 있다
            - 석탄에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.TIN,
            """
            가공하기가 쉬운 광물이다
            보통 합금으로 제작하여 사용된다
            """,
            """
            - 하급 광석에 연금술을 진행하여 얻을 수 있다
            - 구리에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.ZINC,
            """
            매우 흔하지만 가공하기가 어려운 광물이다
            보통 합금으로 사용되거나 도금하는 형태로 사용된다
            """,
            """
            - 하급 광석에 연금술을 진행하여 얻을 수 있다
            - 주석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.LEAD,
            """
            매우 흔하고 유용하게 사용되는 광물이다
            전자제품에는 대부분 포함되며, 보이지 않는 어두운 기운을 막는 효과가 있다고 알려져있다
            """,
            """
            - 하급 광석에 연금술을 진행하여 얻을 수 있다
            - 아연에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.IRON,
            """
            거의 모든 분야에 사용되는 대표적인 금속 광물이다
            철 자체로도 많이 쓰이고, 강철로 합금을 제작하여 사용되기도 한다
            강철 이외에도 다양한 금속들과 함께 쓰인다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 납에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.SILVER,
            """
            대표적인 귀금속 중 하나인 광물이다
            치유 효과를 감소시키는 효과가 있어 무기 제작에도 많이 사용된다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 철에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.GOLD,
            """
            대표적인 귀금속 중 하나인 광물이다
            치유 효과를 증폭시키는 효과가 있어 방어구 내부에 도금하는 형태로 많이 사용된다
            또한 특유의 안정성으로 전자제품에도 도금이 활용되는 경우가 많다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 은에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.GLOW_STONE,
            """
            발광성을 지니는 가장 대표적인 광물이다
            공기 중의 마나를 흡수하여 지속적으로 발광한다고 알려져 있다
            내구성이 매우 떨어지지만 뛰어난 마나 감응도를 보유하여 마법과 관련된 장비에 많이 사용된다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 금에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.RED_IRON,
            """
            철의 변형 형태 중 하나인 광물이다
            철광석으로 존재할 때 특수한 조건에서 마나를 흡수하여 적철이 된다고 알려져 있다
            일반적인 내구성이 떨어진다는 단점이 있지만 마나 감응도가 높고 가벼워 마검사들이 애용하는 광물이다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 발광 석영에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.BLACK_IRON,
            """
            철의 변형 형태 중 하나인 광물이다
            철광석으로 존재할 때 특수한 조건에서 마나를 분출하여 흑철이 된다고 알려져 있다
            무겁고 마나 감응도가 거의 없다시피 하지만, 내구도가 매우 뛰어나 장비에 많이 사용된다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 적철에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.ANTHRACITE,
            """
            연료로 사용할 수 있는 광물이다
            이름대로 일반적인 석탄과 달리 불에 탈 때 연기가 거의 나지 않는다
            일반 석탄과 비교하여 연료 효율이 매우 높고 더 높은 온도를 만들 수 있어 높은 등급의 광물을 가공할 때 쓰인다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 흑철에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.TITANIUM,
            """
            흔하지만 추출과 가공이 매우 어려운 광물이다
            일반적인 철이나 강철과 유사하지만, 무게가 가볍고 녹이 슬지 않아 내구성이 높다
            사용 중 기준 이하의 열을 받으면 오히려 더 단단해지는 특성이 있다
            """,
            """
            - 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 무연탄에 연금술을 진행하여 얻을 수 있다
            """
        );
    }

    private void createItem(ItemType itemType, String description, String acquireWay) {
        Item item = Item.create(itemType, description.trim(), acquireWay.trim());
        this.itemRepository.save(item);
    }

    private void createNpcs() {
        GameMap startVillage = this.gameMapRepository.findByMapType(MapType.START_VILLAGE);

        this.npcRepository.save(Npc.create(NpcType.SYSTEM, this.gameMapRepository.findByMapType(MapType.NONE)));
        this.npcRepository.save(Npc.create(NpcType.NOA, startVillage));
    }

    private void createChats() {
        Npc systemNpc = this.npcRepository.findByNpcType(NpcType.SYSTEM);

        // Tutorial
        {
            Chat chat = Chat.builder()
                .namedChat(NamedChat.TUTORIAL)
                .text("튜토리얼을 진행하시겠습니까?")
                .npc(systemNpc)
                .isForce(true)
                .build();
            this.chatRepository.save(chat);

            Chat endChat = Chat.builder()
                .text("게임에 오신 것을 환영합니다!")
                .build();
            this.chatRepository.save(endChat);

            Chat selectedChat = this.createChatAndAddReply(chat, ReplyType.NO,
                builder -> builder.text("튜토리얼을 스킵합니다").npc(systemNpc));
            selectedChat.setNextChat(endChat);

            selectedChat = this.createChatAndAddReply(chat, ReplyType.YES,
                builder -> builder.text("튜토리얼을 진행합니다").npc(systemNpc));

            chat = this.createNextChat(
                selectedChat,
                builder -> builder.text("게임의 모든 명령어는 " + focus("ㅜ") + " 또는 " + focus("n") + " 으로 시작합니다\n(일부 제외, 대소문자 구분 X)")
            );

            chat = this.createNextChat(
                chat,
                builder -> builder.text("게임의 기본적인 명령어는 " + focus("ㅜ 도움말") + " 로 확인할 수 있습니다")
            );

            chat = this.createNextChat(
                chat,
                builder -> builder.text("플레이어의 정보는 " + focus("ㅜ 정보") + " 로 확인할 수 있습니다")
            );

            chat = this.createNextChat(
                chat,
                builder -> builder.text("맵의 정보는 " + focus("ㅜ 맵") + " 으로 확인할 수 있습니다")
            );

            chat = this.createNextChat(
                chat,
                builder -> builder.text("보다 자세한 정보는 게임을 플레이하며 습득합시다\n일단 " + focus("ㅜ 대화 노아") + " 명령어로 현재 맵에 있는 '노아' NPC와 대화해보세요")
            );

            this.chatRepository.save(chat);
        }
    }

    private Chat createNextChat(Chat chat, Function<Chat.ChatBuilder, Chat.ChatBuilder> buildFunc) {
        Chat nextChat = buildFunc.apply(Chat.builder()).build();
        chat.setNextChat(nextChat);
        this.chatRepository.save(chat);

        return nextChat;
    }

    private Chat createChatAndAddReply(
        Chat chat,
        ReplyType replyType,
        Function<Chat.ChatBuilder, Chat.ChatBuilder> buildFunc
    ) {
        Chat newChat = buildFunc.apply(Chat.builder()).build();
        this.chatRepository.save(newChat);
        chat.getAvailableRepliyMap().put(replyType, newChat.getId());

        return newChat;
    }
}
