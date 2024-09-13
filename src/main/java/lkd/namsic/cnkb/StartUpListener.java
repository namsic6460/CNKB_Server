package lkd.namsic.cnkb;

import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.domain.npc.repository.ChatRepository;
import lkd.namsic.cnkb.domain.npc.repository.NpcRepository;
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

    private final NpcRepository npcRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Npc tutorialNpc = Npc.create(NpcType.SYSTEM);
        this.npcRepository.save(tutorialNpc);

        {
            // 튜토리얼 메세지
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
