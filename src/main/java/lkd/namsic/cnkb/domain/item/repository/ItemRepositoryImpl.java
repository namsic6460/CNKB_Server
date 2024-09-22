package lkd.namsic.cnkb.domain.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkd.namsic.cnkb.domain.item.Item;
import lkd.namsic.cnkb.enums.ItemType;
import lombok.RequiredArgsConstructor;

import static lkd.namsic.cnkb.domain.item.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Item findByItemType(ItemType itemType) {
        return this.queryFactory.selectFrom(item)
            .where(item.itemType.eq(itemType))
            .fetchFirst();
    }
}
