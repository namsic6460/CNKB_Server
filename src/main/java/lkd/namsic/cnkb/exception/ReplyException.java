package lkd.namsic.cnkb.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class ReplyException extends RuntimeException {

    @Nullable
    private final String innerMessage;

    public ReplyException() {
        this("알 수 없는 명령어입니다", null);
    }

    public ReplyException(String message) {
        this(message, null);
    }

    public ReplyException(String message, @Nullable String innerMessage) {
        super(message);
        this.innerMessage = innerMessage;
    }
}
