package lkd.namsic.cnkb.service;

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
import lkd.namsic.cnkb.enums.NpcType;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
    public void startChat(AbstractHandler.UserData userData, Long npcId, Chat chat) {
        Npc npc = this.npcRepository.findById(npcId).orElseThrow();
        this.startChat(userData, npc, chat);
    }

    @Async
    @Transactional
    public void startChat(AbstractHandler.UserData userData, NpcType npcType, NamedChat namedChat) {
        Npc npc = this.npcRepository.findByType(npcType);
        Chat chat = this.chatRepository.findByNamedChat(namedChat);

        this.startChat(userData, npc, chat);
    }

    public void startChat(AbstractHandler.UserData userData, Npc npc, Chat chat) {
        User user = userData.getUser();
        String sender = userData.sender();
        String room = userData.room();
        String npcName = npc.getName();
        String userName = user.getName();

        ActionType chatType = BooleanUtils.isTrue(chat.getIsForce()) ? ActionType.FORCE_CHAT : ActionType.CHAT;
        this.actionHelper.setActionType(user, chatType);

        if (chat.getDelay() != null) {
            Mono.delay(Duration.of(chat.getDelay(), ChronoUnit.MILLIS)).block();
        }

        while (true) {
            String message = this.formatMessage(npcName, userName, chat.getText());
            this.webSocketHandler.sendMessage(new MessageRequest(message, sender, room));

            if (chat.getNextChat() != null) {
                chat = chat.getNextChat();
                if (chat.getDelay() != null) {
                    Mono.delay(Duration.of(chat.getDelay(), ChronoUnit.MILLIS)).block();
                }

                continue;
            } else if (CollectionUtils.isEmpty(chat.getAvailableRepliyMap())) {
                this.userRepository.clearChat(user);
                this.actionHelper.setActionType(user, ActionType.NONE);
            } else {
                ActionType waitType = BooleanUtils.isTrue(chat.getIsForce()) ? ActionType.FORCE_WAIT : ActionType.WAIT;
                this.actionHelper.setActionType(user, waitType);
                this.userRepository.updateChat(user, chat);
            }

            break;
        }
    }

    private String formatMessage(String npcName, String userName, String text) {
        return "[NPC] " + npcName + " -> " + userName + "\n" + text;
    }
}
