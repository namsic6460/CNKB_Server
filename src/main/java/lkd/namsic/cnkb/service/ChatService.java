package lkd.namsic.cnkb.service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import lkd.namsic.cnkb.common.Emoji;
import lkd.namsic.cnkb.component.ActionHelper;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.domain.npc.repository.ChatRepository;
import lkd.namsic.cnkb.domain.npc.repository.NpcRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.dto.MessageRequest;
import lkd.namsic.cnkb.enums.ActionType;
import lkd.namsic.cnkb.enums.NamedChat;
import lkd.namsic.cnkb.enums.ReplyType;
import lkd.namsic.cnkb.enums.domain.NpcType;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChatService {

    private final WebSocketHandler webSocketHandler;
    private final ActionHelper actionHelper;

    private final UserRepository userRepository;
    private final NpcRepository npcRepository;
    private final ChatRepository chatRepository;

    @Async
    @Transactional
    public void startChat(AbstractHandler.UserData userData, NpcType npcType, NamedChat namedChat) {
        Npc npc = this.npcRepository.findByNpcType(npcType);
        Chat chat = this.chatRepository.findByNamedChat(namedChat);

        this.startChat(userData, npc, chat, true);
    }

    public void startChat(AbstractHandler.UserData userData, Npc npc, Chat chat, boolean delay) {
        User user = userData.getUser();
        String sender = userData.sender();
        String room = userData.room();
        String npcName = npc.getNpcType().getValue();
        String userName = user.getName();

        ActionType chatType = chat.isForce() ? ActionType.FORCE_CHAT : ActionType.CHAT;
        this.actionHelper.setActionType(user, chatType);

        if (delay && chat.getDelay() != null) {
            Mono.delay(Duration.of(chat.getDelay(), ChronoUnit.MILLIS)).block();
        }

        while (true) {
            String message = this.formatMessage(npcName, userName, chat.getText());

            if (chat.getNextChat() != null) {
                chat = chat.getNextChat();
                this.webSocketHandler.sendMessage(new MessageRequest(message, sender, room));

                if (chat.getDelay() != null) {
                    Mono.delay(Duration.of(chat.getDelay(), ChronoUnit.MILLIS)).block();
                }

                continue;
            } else if (CollectionUtils.isEmpty(chat.getAvailableRepliyMap())) {
                this.userRepository.clearChat(user);
                this.actionHelper.setActionType(user, ActionType.NONE);
            } else {
                String replyTypes = chat.getAvailableRepliyMap().keySet().stream()
                    .map(ReplyType::getPrimaryValue)
                    .collect(Collectors.joining(" / "));
                message += "\n\n가능한 대답: " + Emoji.focus(replyTypes);

                ActionType waitType = chat.isForce() ? ActionType.FORCE_WAIT : ActionType.WAIT;
                this.actionHelper.setActionType(user, waitType);
                this.userRepository.updateChat(user, chat);
            }

            this.webSocketHandler.sendMessage(new MessageRequest(message, sender, room));
            break;
        }
    }

    private String formatMessage(String npcName, String userName, String text) {
        return "[NPC] " + npcName + " -> " + userName + "\n" + text.replaceAll("\\{name}", userName);
    }
}
