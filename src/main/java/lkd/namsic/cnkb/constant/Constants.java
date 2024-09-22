package lkd.namsic.cnkb.constant;

import java.util.Set;

public class Constants {

    public static final long BOT_ID = 418839927;
    public static final String BOT_NAME = "CNKB";
    public static final Set<Character> PREFIXES = Set.of('n', 'N', 'ㅜ');

    // 커맨드 상 문제가 생길 수 있는 닉네임만 미리 지정. 현 시점에서는 수동으로 처리해도 무방하다
    public static final Set<String> INVALID_NAMES = Set.of("아이템", "item", "장비", "equip", "몬스터", "monster",
        "스킬", "skill", "퀘스트", "quest");

    public static final int ITEMS_PER_PAGE = 50;
}
