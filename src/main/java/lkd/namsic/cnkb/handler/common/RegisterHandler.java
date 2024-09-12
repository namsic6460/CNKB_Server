package lkd.namsic.cnkb.handler.common;

import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.enums.NamedChat;
import lkd.namsic.cnkb.enums.NpcType;
import lkd.namsic.cnkb.exception.ReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterHandler extends AbstractHandler {

    private final UserRepository userRepository;
    private final ChatService chatService;

    @Override
    public List<String> getRootCommands() {
        return List.of("가입", "회원가입", "register");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        if (commands.size() == 1) {
            throw new ReplyException("회원가입 시 닉네임이 필요합니다\n(2~16글자, 띄어쓰기 불가)\n(비속어 등 문제가 될만한 단어 포함 시 제재될 수 있습니다)", null);
        } else if (commands.size() > 2) {
            throw new ReplyException("정확한 명령어를 입력해주세요. 닉네임에 띄어쓰기는 불가합니다");
        }

        if (userData.user() != null) {
            throw new ReplyException("이미 회원가입이 된 계정입니다");
        }

        String name = commands.get(1);
        if (name.length() < 2 || name.length() > 16) {
            throw new ReplyException("닉네임은 2~16글자로 설정할 수 있습니다");
        }

        if (this.userRepository.existsByName(name)) {
            throw new ReplyException("이미 사용중인 닉네임입니다");
        }
    }

    @Nullable
    @Override
    @Transactional
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = User.create(userData.userId(), commands.get(1));
        this.userRepository.save(user);

        UserData newUserData = new UserData(userData.userId(), user, userData.sender(), userData.room());
        this.chatService.startChat(newUserData, NpcType.UNKNOWN, NamedChat.TUTORIAL);

        return new HandleResult("회원가입이 완료되었습니다");
    }
}
