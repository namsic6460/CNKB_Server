package lkd.namsic.cnkb;

import lkd.namsic.cnkb.domain.item.Item;
import lkd.namsic.cnkb.domain.item.repository.ItemRepository;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.domain.npc.repository.ChatRepository;
import lkd.namsic.cnkb.domain.npc.repository.NpcRepository;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.enums.NamedChat;
import lkd.namsic.cnkb.enums.NpcType;
import lkd.namsic.cnkb.enums.ReplyType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

import static lkd.namsic.cnkb.common.Emoji.focus;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class StartUpListener implements ApplicationListener<ApplicationReadyEvent> {

    private final ItemRepository itemRepository;
    private final NpcRepository npcRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.createItems();

        Npc tutorialNpc = Npc.create(NpcType.SYSTEM);
        this.npcRepository.save(tutorialNpc);

        this.createTutorialChats(tutorialNpc);
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
            - 최하급 광석 또는 하급 광석에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.COAL,
            """
            연료로 사용할 수 있는 광물이다
            """,
            """
            - 최하급 광석 또는 하급 광석에 연금술을 진행하여 얻을 수 있다
            - 돌 2개에 연금술을 진행하여 얻을 수 있다
            """
        );

        this.createItem(
            ItemType.COPPER,
            """
            가장 기본적인 광석이다
            단독 또는 합금으로써 다양한 쓰임새를 가지고 있다
            """,
            """
            - 하급 광석 또는 중급 광석에 연금술을 진행하여 얻을 수 있다
            - 석탄 2개에 연금술을 진행하여 얻을 수 있다
            """
        );
    }

    private void createItem(ItemType itemType, String description, String acquireWay) {
        Item item = Item.create(itemType, description.trim(), acquireWay.trim());
        this.itemRepository.save(item);
    }

    private void createTutorialChats(Npc tutorialNpc) {
        Chat chat = Chat.builder()
            .namedChat(NamedChat.TUTORIAL)
            .text("튜토리얼을 진행하시겠습니까?")
            .npc(tutorialNpc)
            .isForce(true)
            .build();
        this.chatRepository.save(chat);

        Chat endChat = Chat.builder()
            .text("게임에 오신 것을 환영합니다!")
            .build();
        this.chatRepository.save(endChat);

        Chat selectedChat = this.createChatAndAddReply(chat, ReplyType.NO,
            builder -> builder.text("튜토리얼을 스킵합니다").npc(tutorialNpc));
        selectedChat.setNextChat(endChat);

        selectedChat = this.createChatAndAddReply(chat, ReplyType.YES,
            builder -> builder.text("튜토리얼을 진행합니다").npc(tutorialNpc));

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
