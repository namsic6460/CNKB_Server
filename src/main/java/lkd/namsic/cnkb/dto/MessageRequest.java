package lkd.namsic.cnkb.dto;

import jakarta.annotation.Nullable;

public record MessageRequest(
    String message,
    String innerMessage,
    String sender,
    @Nullable String room
) {

    public MessageRequest(String message, String sender, @Nullable String room) {
        this(message, null, sender, room);
    }
}
