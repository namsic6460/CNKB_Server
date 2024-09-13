package lkd.namsic.cnkb.dto;

import jakarta.annotation.Nullable;

import java.util.Optional;

public record MessageRequest(
    String message,
    String innerMessage,
    String sender,
    @Nullable String room
) {
    public MessageRequest(String message, String innerMessage, String sender, String room) {
        this.message = message.trim();
        this.innerMessage = Optional.ofNullable(innerMessage)
            .map(String::trim)
            .orElse(null);

        this.sender = sender;
        this.room = room;
    }

    public MessageRequest(String message, String sender, String room) {
        this(message, null, sender, room);
    }
}
