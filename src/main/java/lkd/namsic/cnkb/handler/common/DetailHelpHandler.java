package lkd.namsic.cnkb.handler.common;

import lkd.namsic.cnkb.handler.AbstractHandler;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetailHelpHandler extends AbstractHandler {

    @Override
    public List<String> getRootCommands() {
        return List.of("??", "hh");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        // DO NOTHING
    }

    @Nullable
    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        return new HandleResult(
            """
            =====명령어 목록=====
            모든 명령어는 앞쪽에 「n」 또는 「ㅜ」 를 붙여야 합니다
            「*」이 붙은 명령어는 언제나 사용이 가능한 명령어입니다
            () : 필수 명령어, [] : 필수X 명령어, {} : 직접 입력
            """,
            """
            [봇 버전 : 0.0.1 - ALPHA]
            ⚫ *(도움말/명령어/?/help/h) : 도움말을 표시합니다
            ⚫ *(??/hh) : 상세 도움말을 표시합니다
            ⚫ (회원가입/가입/register) ({닉네임}) : 회원가입을 진행합니다
            ⚫ (개발자/dev) : 개발자 정보를 표시합니다
            ⚫ (규칙/룰/rule) : 게임 규칙을 표시합니다 (규칙 미숙지로 인한 제재는 본인의 책임입니다)
            -----
            ⚫ (정보/info/i) [{닉네임}] : 플레이어의 정보를 표시합니다
            ⚫ (가방/인벤토리/인벤/inventory/inven) [{페이지}] : 인벤토리를 표시합니다
            ⚫ (대화/chat) ({NPC 이름}) : NPC와 대화합니다
            """
        );
    }
}