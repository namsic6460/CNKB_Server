package lkd.namsic.cnkb.handler.common;

import lkd.namsic.cnkb.handler.AbstractHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelpHandler extends AbstractHandler {

    @Override
    public List<String> getRootCommands() {
        return List.of("도움말", "명령어", "?", "help", "h");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        // DO NOTHING
    }

    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        return new HandleResult(
            """
            =====명령어 목록=====
            도움말에 표시되는 () 는 입력하지 않습니다
            이곳에 없는 명령어는 상세 도움말로 확인해주세요
            """,
            """
                [봇 버전 : 0.0.1 - ALPHA]

                ❓ ㅜ 도움말 -> 도움말을 표시합니다

                ❓ ㅜ ?? -> 상세 도움말을 표시합니다

                👋 ㅜ 회원가입 (이름) -> 회원가입을 합니다

                💻 ㅜ 개발자 -> 개발자 정보를 표시합니다

                📝 ㅜ 규칙 -> 게임 규칙을 표시합니다

                👦 ㅜ 정보 (다른 플레이어 이름) -> 자신 또는 다른 플레이어의 정보를 표시합니다

                💼 ㅜ 인벤토리 -> 인벤토리를 표시합니다

                💬 ㅜ 대화 (Npc 이름) -> Npc 와 대화합니다

                \uD83C\uDFED ㅜ 채굴기 -> 채굴기의 아이템을 습득합니다

                🧪 ㅜ 연금술 (아이템 이름) (개수) -> 광석을 광물로 변환합니다
                """
        );
    }
}
