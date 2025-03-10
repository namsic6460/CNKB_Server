package lkd.namsic.cnkb.exception;

public class DataNotFoundException extends ReplyException {

    private DataNotFoundException(String component) {
        super("알 수 없는 " + component + "입니다");
    }

    public static DataNotFoundException item() {
        return new DataNotFoundException("아이템");
    }

    public static DataNotFoundException npc() {
        return new DataNotFoundException("NPC ");
    }

    public static DataNotFoundException map() {
        return new DataNotFoundException("맵");
    }

    public static DataNotFoundException minerStat() {
        return new DataNotFoundException("채굴기 스탯");
    }
}
