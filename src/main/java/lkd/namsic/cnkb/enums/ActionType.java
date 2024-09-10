package lkd.namsic.cnkb.enums;

import lombok.Getter;

@Getter
public enum ActionType {

    NONE("-"),
    FORCE_CHAT("대화(강제 종료 불가)"),
    CHAT("대화"),
    FORCE_WAIT("대답 대기(강제 종료 불가)"),
    WAIT("대답 대기"),
    ;

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public boolean isWait() {
        return this == FORCE_WAIT || this == WAIT;
    }
}
