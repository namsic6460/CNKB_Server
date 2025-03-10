package lkd.namsic.cnkb.service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lkd.namsic.cnkb.component.ActionHelper;
import lkd.namsic.cnkb.constant.Constants;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.domain.npc.repository.ChatRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.dto.KakaoMessage;
import lkd.namsic.cnkb.dto.MessageRequest;
import lkd.namsic.cnkb.enums.ReplyType;
import lkd.namsic.cnkb.exception.ReplyException;
import lkd.namsic.cnkb.exception.SkipException;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final Map<String, AbstractHandler> handlerMap = new HashMap<>();

    private final WebSocketHandler handler;
    private final ChatService chatService;
    private final ActionHelper actionHelper;

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    private final List<AbstractHandler> handlers;

    @PostConstruct
    public void postConstruct() {
        this.handlers.forEach(handler ->
            handler.getRootCommands().forEach(rootCommand -> this.handlerMap.put(rootCommand, handler))
        );
    }

    @Async
    @Transactional
    public void processMessage(KakaoMessage kakaoMessage) {
        String message = kakaoMessage.message().trim();
        String sender = kakaoMessage.sender();
        String room = kakaoMessage.room();
        long userId = kakaoMessage.userId();

        User user = this.userRepository.findById(userId).orElse(null);

        // "{prefix} {command}" 형태인지 검증
        if (message.length() < 3 || message.charAt(1) != ' ' || !Constants.PREFIXES.contains(message.charAt(0))) {
            if (user == null || user.getChat() == null) {
                return;
            }

            ReplyType replyType = ReplyType.parse(message);
            if (!this.actionHelper.getActionType(user).isWait() || replyType == null) {
                return;
            }

            AbstractHandler.UserData userData = new AbstractHandler.UserData(userId, user, sender, room);
            Chat chat = this.chatRepository.findById(user.getChat().getId()).orElseThrow();

            Long nextChatId = chat.getAvailableRepliyMap().get(replyType);
            if (nextChatId == null) {
                return;
            }

            Chat nextChat = this.chatRepository.findByIdWithNpc(nextChatId);
            this.chatService.startChat(userData, nextChat.getNpc(), nextChat, false);

            return;
        }

        try {
            List<String> commands = Arrays.stream(message.substring(2).split(" "))
                .map(String::toLowerCase)
                .toList();

            AbstractHandler handler = this.handlerMap.get(commands.getFirst());
            if (handler == null) {
                log.warn("Unknown root command {}", commands.getFirst());
                return;
            }

            AbstractHandler.UserData userData = new AbstractHandler.UserData(userId, user, sender, room);
            handler.verify(commands, userData);

            AbstractHandler.HandleResult handleResult = handler.handle(commands, userData);
            if (handleResult == null) {
                return;
            }

            String resultMessage = handleResult.message();
            if (user != null || handleResult.user() != null) {
                user = Optional.ofNullable(user).orElseGet(handleResult::user);
                resultMessage = this.getUserReplyPrefix(user) + resultMessage;
            }

            this.handler.sendMessage(new MessageRequest(resultMessage, handleResult.innerMessage(), sender, room));
        } catch (UserReplyException e) {
            assert user != null;

            String resultMessage = this.getUserReplyPrefix(user) + e.getMessage();
            this.handler.sendMessage(new MessageRequest(resultMessage, e.getInnerMessage(), sender, room));
        } catch (ReplyException e) {
            this.handler.sendMessage(new MessageRequest(e.getMessage(), e.getInnerMessage(), sender, room));
        } catch (SkipException e) {
            // DO NOTHING - JUST SKIP
        } catch (NumberFormatException e) {
            this.handler.sendMessage(new MessageRequest("숫자를 입력해주세요", null, sender, room));
        } catch (Exception e) {
            log.error("Failed to handle commands", e);
            this.handler.sendMessage(new MessageRequest("처리 중 에러가 발생했습니다. 관리자를 호출해주세요", sender, room));
        }
    }

    private String getUserReplyPrefix(User user) {
        return "[" + user.getTitle() + "] " + user.getName() + "(Lv." + user.getLv() + ")\n";
    }
}
