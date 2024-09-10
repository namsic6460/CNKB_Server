package lkd.namsic.cnkb.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class ReplyException extends RuntimeException {

    @Nullable
    private final String innerMessage;

    public ReplyException(String message) {
        this(message, null);
    }

    public ReplyException(String message, @Nullable String innerMessage) {
        super(message);
        this.innerMessage = innerMessage;
    }
}
