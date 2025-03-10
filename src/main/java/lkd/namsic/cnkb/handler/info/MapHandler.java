package lkd.namsic.cnkb.handler.info;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.map.repository.GameMapRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.handler.AbstractHandler;
import lkd.namsic.cnkb.utils.LevelUtils;
import lkd.namsic.cnkb.utils.MathUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapHandler extends AbstractHandler {

    private final GameMapRepository gameMapRepository;

    @Override
    public List<String> getRootCommands() {
        return List.of("맵", "map");
    }

    @Override
    public void verify(List<String> commands, UserData userData) {
        this.checkUser(userData);
        this.checkMaxLength(commands, 1);
    }

    @Override
    public HandleResult handle(List<String> commands, UserData userData) {
        User user = userData.getUser();
        double maxMoveDistance = LevelUtils.getMaxMoveDistance(user.getLv());
        int ceilMaxMoveDistance = (int) Math.ceil(maxMoveDistance);

        Location location = user.getCurrentGameMap().getLocation();
        int maxX = location.x() + ceilMaxMoveDistance;
        int maxY = location.y() + ceilMaxMoveDistance;

        List<GameMap> availableMaps = new ArrayList<>();
        for (int x = location.x() - ceilMaxMoveDistance; x <= maxX; x++) {
            for (int y = location.y() - ceilMaxMoveDistance; y <= maxY; y++) {
                if (x == location.x() && y == location.y()) {
                    continue;
                }

                try {
                    GameMap targetMap = this.gameMapRepository.findByLocation(new Location(x, y)).orElseThrow();
                    if (MathUtils.getDistance(location, targetMap.getLocation()) <= maxMoveDistance) {
                        availableMaps.add(targetMap);
                    }
                } catch (NoSuchElementException e) {
                    // DO NOTHING
                }
            }
        }

        StringBuilder builder = new StringBuilder("===이동 가능한 맵 목록===");
        availableMaps.forEach(availableMap -> {
            Location mapLocation = availableMap.getLocation();

            builder
                .append("\n")
                .append(availableMap.getName())
                .append("(")
                .append(mapLocation.x())
                .append(", ")
                .append(mapLocation.y())
                .append(")");
        });

        return new HandleResult(builder.toString());
    }
}
