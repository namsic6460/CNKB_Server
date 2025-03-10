package lkd.namsic.cnkb.domain.map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lkd.namsic.cnkb.config.converter.LocationConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.item.MapLimitItem;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.dto.Location;
import lkd.namsic.cnkb.enums.domain.MapType;
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
    @Convert(converter = LocationConverter.class)
    private Location location;

    @Column(nullable = false)
    private int limitLv;

    @ManyToMany
    @JoinTable(
        name = "passed_users",
        joinColumns = @JoinColumn(name = "map_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private final List<User> passedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "key.map", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MapLimitItem> mapLimitItems = new ArrayList<>();

    public static GameMap create(MapType mapType, Location location, int limitLv) {
        GameMap gameMap = new GameMap();
        gameMap.name = mapType.getValue();
        gameMap.location = location;
        gameMap.limitLv = limitLv;

        return gameMap;
    }
}
