package lkd.namsic.cnkb.domain.item.repository;

import static lkd.namsic.cnkb.domain.item.QUserInventory.userInventory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lkd.namsic.cnkb.constant.Constants;
import lkd.namsic.cnkb.domain.item.QItem;
import lkd.namsic.cnkb.domain.item.UserInventory;
import lkd.namsic.cnkb.domain.item.dto.InventoryItemDto;
import lkd.namsic.cnkb.domain.item.dto.QInventoryItemDto;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.InventoryType;
import lkd.namsic.cnkb.enums.domain.ItemType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserInventoryRepositoryImpl implements InventoryRepositoryCustom<User> {

    private final JPAQueryFactory queryFactory;

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.USER;
    }

    @Override
    public List<InventoryItemDto> getItems(User user, List<ItemType> priorityTypes) {
        QItem item = QItem.item;

        return this.queryFactory
            .select(new QInventoryItemDto(
                item.itemType,
                userInventory.count
            ))
            .from(userInventory)
            .where(
                userInventory.key.user.eq(user),
                item.itemType.in(priorityTypes)
            )
            .leftJoin(userInventory.key.item, item)
            .orderBy(item.itemType.asc())
            .fetch();
    }

    @Override
    public List<InventoryItemDto> getItems(User user, int page) {
        QItem item = QItem.item;

        return this.queryFactory
            .select(new QInventoryItemDto(
                item.itemType,
                userInventory.count
            ))
            .from(userInventory)
            .where(userInventory.key.user.eq(user), userInventory.count.gt(0))
            .leftJoin(userInventory.key.item, item)
            .orderBy(item.itemType.asc())
            .offset(Constants.ITEMS_PER_PAGE * (page - 1L))
            .limit(Constants.ITEMS_PER_PAGE)
            .fetch();
    }

    @Override
    public int getItemCount(User user) {
        return this.queryFactory.selectFrom(userInventory)
            .where(userInventory.key.user.eq(user))
            .fetch()
            .size();
    }

    @Override
    public int getItemCount(User user, ItemType itemType) {
        QItem item = QItem.item;

        return Optional
            .ofNullable(
                this.queryFactory.selectFrom(userInventory)
                    .where(
                        userInventory.key.user.eq(user),
                        item.itemType.eq(itemType)
                    )
                    .leftJoin(userInventory.key.item, item)
                    .fetchFirst()
            )
            .map(UserInventory::getCount)
            .orElse(0);
    }

    @Override
    public InventoryItemDto getItem(User user, ItemType itemType) {
        QItem item = QItem.item;

        return this.queryFactory
            .select(new QInventoryItemDto(
                item.itemType,
                userInventory.count
            ))
            .from(userInventory)
            .where(
                userInventory.key.user.eq(user),
                userInventory.key.item.itemType.eq(itemType)
            )
            .leftJoin(userInventory.key.item, item)
            .fetchFirst();
    }

    @Override
    public void setCount(User user, ItemType itemType, int count) {
        this.queryFactory.update(userInventory)
            .set(userInventory.count, count)
            .where(
                userInventory.key.user.eq(user),
                userInventory.key.item.itemType.eq(itemType)
            )
            .execute();
    }
}
