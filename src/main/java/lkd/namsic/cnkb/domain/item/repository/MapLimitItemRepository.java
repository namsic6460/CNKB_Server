package lkd.namsic.cnkb.domain.item.repository;

import lkd.namsic.cnkb.domain.item.MapLimitItem;
import lkd.namsic.cnkb.domain.item.MapLimitItemKey;
import lkd.namsic.cnkb.domain.map.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapLimitItemRepository extends JpaRepository<MapLimitItem, MapLimitItemKey>, InventoryRepositoryCustom<GameMap> {
}
