package lkd.namsic.cnkb.handler.common;

import java.util.List;
import lkd.namsic.cnkb.handler.AbstractHandler;
import org.springframework.stereotype.Component;

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
            ⚫ (채굴기/채굴/miner) : 채굴기의 아이템을 습득합니다
            ⚫ (채굴기/채굴/miner) (업그레이드/강화/upgrade) ({채굴기 스탯}) : 채굴기의 스탯을 올립니다
            ⚫ (연금술/연금/alchemy/a) ({사용할 광석 이름}) [{수량}] : 광석을 광물로 변환합니다 (수량: 변환에 사용할 광석 수량)
            ⚫ (연금술/연금/alchemy/a) ({변환될 광물 이름}) [{수량}] : 이전 등급의 광물을 사용하여 다음 등급의 광물을 생성합니다 (수량: 변환 후 결과물의 수량)
            ⚫ (연금술/연금/alchemy/a) (역/reverse/r) ({변환될 광물 이름}) [{수량}] : 다음 등급의 광물을 사용하여 이전 등급의 광물을 생성합니다 (수량: 변환 후 결과물의 수량)
            ⚫ (연금술/연금/alchemy/a) (확인/체크/check/c) ({변환될 광물 이름}) : 해당 광물로 변환하는 연금술이 성공할 확률을 확인합니다
            ⚫ (맵/map) : 이동 가능한 맵 이름을 확인합니다
            ⚫ (이동/move) ({맵 이름}) : 해당 맵으로 이동합니다
            ⚫ (이동/move) ({x 좌표}) ({y 좌표}) : 해당 좌표의 맵으로 이동합니다
            """
        );
    }
}
