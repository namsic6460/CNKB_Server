package lkd.namsic.cnkb.exception;

import org.jetbrains.annotations.Nullable;

public class UserReplyException extends ReplyException {

    public UserReplyException() {
        super();
    }

    public UserReplyException(String message) {
        super(message);
    }

    public UserReplyException(String message, @Nullable String innerMessage) {
        super(message, innerMessage);
    }
}
