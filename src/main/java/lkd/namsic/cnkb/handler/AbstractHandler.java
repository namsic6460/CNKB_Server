package lkd.namsic.cnkb.handler;

import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.exception.SkipException;
import lkd.namsic.cnkb.exception.UserReplyException;

public abstract class AbstractHandler {

    public abstract List<String> getRootCommands();
    public abstract void verify(List<String> commands, UserData userData);

    /**
     * @param commands Lowercase commands.<br>Excludes prefix("n arg1 arg2" -> [arg1, arg2])
     */
    @Nullable public abstract HandleResult handle(List<String> commands, UserData userData);

    protected void checkUser(UserData userData) {
        if (userData.user == null) {
            // 회원가입을 하라는 메세지를 보내줘도 되지만, 악용하는 유저가 있을 수 있으니 그냥 무시
            throw new SkipException();
        }
    }

    protected void checkMinLength(List<String> commands, int minLength) {
        if (commands.size() < minLength) {
            throw new UserReplyException("정확하지 않은 명령어입니다\n도움말 확인 후 다시 입력해주세요");
        }
    }

    protected void checkMaxLength(List<String> commands, int maxLength) {
        if (commands.size() > maxLength) {
            throw new UserReplyException("정확하지 않은 명령어입니다\n도움말 확인 후 다시 입력해주세요");
        }
    }

    public record UserData(
        long userId,
        @Nullable User user,
        String sender,
        String room
    ) {
        public User getUser() {
            return Objects.requireNonNull(this.user);
        }
    }

    public record HandleResult(
        String message,
        @Nullable String innerMessage,

        // 회원가입 시 생성된 유저
        User user
    ) {
        public HandleResult(String message) {
            this(message, null, null);
        }

        public HandleResult(String message, String innerMessage) {
            this(message, innerMessage, null);
        }

        public static HandleResult itemGathered(ItemType itemType, int gatheredCount, int currentCount) {
            String itemName = itemType.getValue();
            return new HandleResult(itemName + " " + gatheredCount + "개를 획득하였습니다\n현재 " + itemName + " 개수: " + currentCount);
        }
    }
}
