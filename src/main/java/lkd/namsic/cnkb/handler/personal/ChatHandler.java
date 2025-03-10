package lkd.namsic.cnkb.handler.personal;

import jakarta.annotation.Nullable;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.Npc;
import lkd.namsic.cnkb.domain.npc.repository.ChatRepository;
import lkd.namsic.cnkb.domain.npc.repository.NpcRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.domain.NpcType;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatHandler extends AbstractHandler {

    private final NpcRepository npcRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    private final SecureRandom random = new SecureRandom();

    @Override
    public List<String> getRootCommands() {
        return List.of("대화", "chat");
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
        String npcName = commands.stream()
            .skip(1)
            .collect(Collectors.joining(" "));

        NpcType npcType = NpcType.find(npcName);
        Npc npc = this.npcRepository.findByNpcTypeAndGameMap(npcType, user.getCurrentGameMap())
            .orElseThrow(() -> new UserReplyException("현재 맵에 없는 NPC 입니다"));

        Chat chat;
        if (user.getMetNpcTypes().contains(npcType)) {
            List<Chat> notConnectedChats = this.chatRepository.findNotConnectedChats(npc);
            if (notConnectedChats.isEmpty()) {
                throw new IllegalStateException();
            }

            chat = notConnectedChats.get(this.random.nextInt(notConnectedChats.size()));
        } else {
            chat = this.chatRepository.findFirstChat(npc);
        }

        this.chatService.startChat(userData, npc, chat, false);
        return null;
    }
}
