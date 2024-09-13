package lkd.namsic.cnkb.handler.common;

import lkd.namsic.cnkb.handler.AbstractHandler;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DevHandler extends AbstractHandler {

    @Override
    public List<String> getRootCommands() {
        return List.of("개발자", "dev");
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
            닉네임 : 남식(namsic)
            이메일 : namsic6460@gmail.com
            문의 : 개인 카카오톡 또는 이메일
            """
        );
    }
}