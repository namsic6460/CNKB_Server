package lkd.namsic.cnkb.domain.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lkd.namsic.cnkb.domain.map.GameMap;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MapLimitItemKey extends InventoryKey<GameMap> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private GameMap map;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public static MapLimitItemKey create(GameMap map, Item item) {
        MapLimitItemKey key = new MapLimitItemKey();
        key.map = map;
        key.item = item;

        return key;
    }

    @Override
    public GameMap getTarget() {
        return this.map;
    }
}
