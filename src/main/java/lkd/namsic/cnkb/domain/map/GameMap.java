package lkd.namsic.cnkb.domain.map;

import jakarta.persistence.*;
import lkd.namsic.cnkb.config.converter.LocationConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.enums.MapType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Convert(converter = LocationConverter.class)
    private Location location;

    @Column(nullable = false)
    private int limitLv;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @OneToMany(mappedBy = "key.user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<User> passedUsers = new ArrayList<>();

    public static GameMap create(MapType mapType, Location location, int limitLv) {
        GameMap gameMap = new GameMap();
        gameMap.name = mapType.getValue();
        gameMap.location = location;
        gameMap.limitLv = limitLv;

        return gameMap;
    }

    public void addPassedUser(User user) {
        if (!this.passedUsers.contains(user)) {
            this.passedUsers.add(user);
        }
    }
}
