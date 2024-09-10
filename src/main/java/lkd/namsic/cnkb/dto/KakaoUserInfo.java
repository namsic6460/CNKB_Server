package lkd.namsic.cnkb.dto;

import jakarta.annotation.Nullable;

public record KakaoUserInfo(
    @Nullable String room,
    String sender,
    boolean isGroupChat,
    long userId,
    long chatId
) {
}
