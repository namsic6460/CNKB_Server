package lkd.namsic.cnkb.domain.map;

import jakarta.persistence.*;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.enums.MapType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameMap extends AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 31, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    public static GameMap create(MapType mapType, int x, int y) {
        GameMap gameMap = new GameMap();
        gameMap.name = mapType.getValue();
        gameMap.x = x;
        gameMap.y = y;

        return gameMap;
    }
}
