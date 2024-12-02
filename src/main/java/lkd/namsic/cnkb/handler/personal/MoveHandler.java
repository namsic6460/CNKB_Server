package lkd.namsic.cnkb.handler.personal;

import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.map.repository.GameMapRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.exception.UserReplyException;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.utils.LevelUtils;
import lkd.namsic.cnkb.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MoveHandler extends AbstractHandler {

    private final GameMapRepository gameMapRepository;
    private final UserRepository userRepository;

    @Override
    public List<String> getRootCommands() {
        return List.of("이동", "move");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        this.checkLength(commands, 3);
        this.checkUser(userData);
    }

    @Nullable
    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = userData.getUser();
        GameMap gameMap = user.getGameMap();

        UserReplyException parseFailException = new UserReplyException("이동하려는 좌표는 정수여야 합니다");

        int currentX = gameMap.getX();
        int currentY = gameMap.getY();

        int targetX = Optional.of(Integer.parseInt(commands.get(1))).orElseThrow(() -> parseFailException);
        int targetY = Optional.of(Integer.parseInt(commands.get(2))).orElseThrow(() -> parseFailException);

        double maxMoveDistance = LevelUtils.getMaxMoveDistance(user.getLv());
        double moveDistance = MathUtils.getDistance(currentX, currentY, targetX, targetY);

        if (moveDistance < maxMoveDistance) {
            throw new UserReplyException("이동할 수 없는 거리입니다\n(이동 거리 : " + moveDistance + ", 이동 가능 거리 : " + maxMoveDistance + ")");
        }

        GameMap targetMap = gameMapRepository.findByXY(targetX, targetY).orElseThrow(() -> new UserReplyException("해당 좌표에는 맵이 없습니다"));

        userRepository.updateGameMap(user, targetMap);

        return null;
    }
}
