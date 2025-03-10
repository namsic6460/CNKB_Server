package lkd.namsic.cnkb.domain.item.repository;

import static lkd.namsic.cnkb.domain.item.QMapLimitItem.mapLimitItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lkd.namsic.cnkb.domain.item.MapLimitItem;
import lkd.namsic.cnkb.domain.item.QItem;
import lkd.namsic.cnkb.domain.item.dto.InventoryItemDto;
import lkd.namsic.cnkb.domain.item.dto.QInventoryItemDto;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.enums.InventoryType;
import lkd.namsic.cnkb.enums.domain.ItemType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapLimitItemRepositoryImpl implements InventoryRepositoryCustom<GameMap> {

    private final JPAQueryFactory queryFactory;

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.MAP;
    }

    @Override
    public List<InventoryItemDto> getItems(GameMap gameMap, List<ItemType> priorityTypes) {
        return Collections.emptyList();
    }

    @Override
    public List<InventoryItemDto> getItems(GameMap gameMap, int page) {
        QItem item = QItem.item;

        return this.queryFactory
            .select(new QInventoryItemDto(
                item.itemType,
                mapLimitItem.count
            ))
            .from(mapLimitItem)
            .where(mapLimitItem.key.map.eq(gameMap), mapLimitItem.count.gt(0))
            .leftJoin(mapLimitItem.key.item, item)
            .orderBy(item.itemType.asc())
            .fetch();
    }

    @Override
    public int getItemCount(GameMap map) {
        return this.queryFactory.selectFrom(mapLimitItem)
            .where(mapLimitItem.key.map.eq(map))
            .fetch()
            .size();
    }

    @Override
    public int getItemCount(GameMap gameMap, ItemType itemType) {
        QItem item = QItem.item;

        return Optional
            .ofNullable(
                this.queryFactory.select(mapLimitItem)
                    .where(
                        mapLimitItem.key.map.eq(gameMap),
                        item.itemType.eq(itemType)
                    )
                    .leftJoin(mapLimitItem.key.item, item)
                    .fetchFirst()
            )
            .map(MapLimitItem::getCount)
            .orElse(0);
    }

    @Override
    public InventoryItemDto getItem(GameMap gameMap, ItemType itemType) {
        QItem item = QItem.item;

        return this.queryFactory
            .select(new QInventoryItemDto(
                item.itemType,
                mapLimitItem.count
            ))
            .from(mapLimitItem)
            .where(
                mapLimitItem.key.map.eq(gameMap),
                mapLimitItem.key.item.itemType.eq(itemType)
            )
            .fetchFirst();
    }

    @Override
    public void setCount(GameMap gameMap, ItemType itemType, int count) {
        this.queryFactory.update(mapLimitItem)
            .set(mapLimitItem.count, count)
            .where(
                mapLimitItem.key.map.eq(gameMap),
                mapLimitItem.key.item.itemType.eq(itemType)
            )
            .execute();
    }
}
