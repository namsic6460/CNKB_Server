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

@Component
@Profile("dev")
@RequiredArgsConstructor
public class StartUpListener implements ApplicationListener<ApplicationReadyEvent> {

    private final NpcRepository npcRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Npc tutorialNpc = Npc.create(NpcType.UNKNOWN);
        this.npcRepository.save(tutorialNpc);

        {
            // 튜토리얼 메세지
            Chat chat = Chat.builder()
                .namedChat(NamedChat.TUTORIAL)
                .text("튜토리얼 시작 메세지")
                .npc(tutorialNpc)
                .build();

            chat = this.createNextChat(
                chat,
                builder -> builder.text("튜토리얼 두번쨰 메세지. 응답을 입력해주세요")
            );
            this.chatRepository.save(chat);

            Chat endChat = Chat.builder()
                .text("튜토리얼 테스트 종료")
                .delay(3000L)
                .build();

            Chat selectedChat = this.createChatAndAddReply(chat, ReplyType.YES,
                builder -> builder.text("YES 선택 시 응답 메세지").npc(tutorialNpc));
            selectedChat.setNextChat(endChat);

            selectedChat = this.createChatAndAddReply(chat, ReplyType.NO,
                builder -> builder.text("NO 선택 시 응답 메세지").npc(tutorialNpc));
            selectedChat.setNextChat(endChat);

            selectedChat = this.createChatAndAddReply(chat, ReplyType.ONE,
                builder -> builder.text("ONE 선택 시 응답 메세지").npc(tutorialNpc));
            selectedChat.setNextChat(endChat);

            selectedChat = this.createChatAndAddReply(chat, ReplyType.TWO,
                builder -> builder.text("TWO 선택 시 응답 메세지").npc(tutorialNpc));
            selectedChat.setNextChat(endChat);

            this.chatRepository.save(endChat);
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
        chat.getAvailableReplieMap().put(replyType, newChat.getId());

        return newChat;
    }
}
