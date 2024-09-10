package lkd.namsic.cnkb.dto;

import jakarta.annotation.Nullable;

public record KakaoMessage(
    String message,
    String sender,
    @Nullable String room,
    boolean isGroupChat,
    long userId,
    long chatId
) {

    public KakaoMessage(String message, KakaoUserInfo userInfo) {
        this(message, userInfo.sender(), userInfo.room(), userInfo.isGroupChat(), userInfo.userId(), userInfo.chatId());
    }
}
