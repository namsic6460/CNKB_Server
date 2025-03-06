package lkd.namsic.cnkb.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.map.GameMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "map_limit_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MapLimitItem extends AbstractEntity implements Inventory<GameMap> {

    @EmbeddedId
    private MapLimitItemKey key;

    @Column(nullable = false)
    private int count;

    public static MapLimitItem create(GameMap map, Item item, int count) {
        MapLimitItem mapLimitItem = new MapLimitItem();
        mapLimitItem.key = MapLimitItemKey.create(map, item);
        mapLimitItem.count = count;

        return mapLimitItem;
    }
}
