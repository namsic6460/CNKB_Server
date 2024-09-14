package lkd.namsic.cnkb.handler.common;

import lkd.namsic.cnkb.handler.AbstractHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleHandler extends AbstractHandler {

    @Override
    public List<String> getRootCommands() {
        return List.of("룰", "rule");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        // DO NOTHING
    }

    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        return new HandleResult(
            "[게임 규칙]",
            """
            1. 매크로 및 그와 유사한 모든 행위를 금지한다
            2. 게임 버그의 악용을 금지한다 (버그 제보 시 보상)
            3. 무차별 PVP를 금지한다
            4. 티밍을 허용한다
            5. 모든 플레이어는 재판을 요청할 수 있다
            6. 납득 가능한 이유가 없는 형태의 도배를 금지한다
            7. 본 규칙을 숙지하지 않아 발생한 문제는 개인의 책임이다
            8. 위 규칙 및 그 외 사항에 대한 판단은 게임의 제작자의 판단에 따른다

            =====

            [재판]
            - 플레이어는 1일에 한 번 재판을 요청할 수 있다
            - 재판의 대상은 다른 플레이어(들) 또는 제작자의 처벌에 한정한다
            - 처벌에 대한 재반은 처벌이 일어나기 전과 후(24시간 이내) 모두 요청할 수 있다
            - 재판은 모두 게임 제작자의 주관적 판단에 의한다
            - 재판의 대상으로 지목된 플레이어(들)는 반드시 재판에 참여해야 하며, 불참 시 패소한다
            - 재판의 대상으로 다수의 플레이어가 지목되었을 시 최소 1인만 참여하면 된다

            =====

            [PVP]
            - 본 게임은 PVP를 허용한다
            - 플레이어는 특별한 이유 없이 다른 플레이어를 공격할 수 있다
            - 특정 플레이어만 반복해서 공격하는 행위는 금지한다. 일부 오차(+-)가 있어도 무관하나, 기본적인 기준은 3킬/1시간 으로 한다
            - 특별한 이유가 있을 시 기본적인 기준을 넘은 공격 행위를 허용한다
            - PVP를 당하는 것은 재판 사유에 해당한다. 피해자는 기준 이상의 피해를, 가해자는 기준 이하의 피해 또는 특별한 사유에 대한 해명을 제시해야 한다.
            
            =====
            
            [개발자의 말]
            다 필요없고 그냥 적당히 하라는 말입니다
            분쟁이 생기면 가장 머리가 아픈건 저이니 제발 적당히 알잘딱 합시다
            """
        );
    }
}
