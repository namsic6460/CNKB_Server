package lkd.namsic.cnkb.handler;

import jakarta.annotation.Nullable;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.exception.SkipException;

import java.util.List;
import java.util.Objects;

public abstract class AbstractHandler {

    public abstract List<String> getRootCommands();
    public abstract void verify(List<String> commands, UserData userData);
    @Nullable public abstract HandleResult handle(List<String> commands, UserData userData);

    protected void checkUser(UserData userData) {
        if (userData.user == null) {
            throw new SkipException();
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
    }
}
