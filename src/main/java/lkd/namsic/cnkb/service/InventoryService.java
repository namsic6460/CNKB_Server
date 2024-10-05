package lkd.namsic.cnkb.service;

import jakarta.annotation.PostConstruct;
import lkd.namsic.cnkb.domain.item.Inventory;
import lkd.namsic.cnkb.domain.item.InventoryKey;
import lkd.namsic.cnkb.domain.item.Item;
import lkd.namsic.cnkb.domain.item.UserInventory;
import lkd.namsic.cnkb.domain.item.dto.InventoryItemDto;
import lkd.namsic.cnkb.domain.item.repository.InventoryRepositoryCustom;
import lkd.namsic.cnkb.domain.item.repository.ItemRepository;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.enums.InventoryType;
import lkd.namsic.cnkb.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InventoryService {

    private final Map<InventoryType, InventoryRepositoryCustom<?>> inventoryRepositoryMap = new HashMap<>();

    private final List<InventoryRepositoryCustom<?>> inventoryRepositories;
    private final ItemRepository itemRepository;

    @SuppressWarnings("unchecked")
    private <T> InventoryRepositoryCustom<T> getRepository(T target) {
        InventoryType inventoryType = InventoryType.find(target);
        return (InventoryRepositoryCustom<T>) this.inventoryRepositoryMap.get(inventoryType);
    }

    @PostConstruct
    public void postConstruct() {
        this.inventoryRepositories.forEach(
            repository -> this.inventoryRepositoryMap.put(repository.getInventoryType(), repository)
        );
    }

    @SuppressWarnings("unchecked")
    private <T> void create(T target, ItemType itemType, int count) {
        Class<?> c = target.getClass();
        InventoryRepositoryCustom<T> repository = this.getRepository(target);

        Inventory<T> inventory = null;
        if (InventoryType.USER.getC().isAssignableFrom(c)) {
            Item item = this.itemRepository.findByItemType(itemType);
            inventory = (Inventory<T>) UserInventory.create((User) target, item, count);
        }

        if (inventory == null) {
            throw new IllegalArgumentException("Unknown target type - " + target.getClass().getName());
        }

        ((JpaRepository<Inventory<T>, InventoryKey<T>>) repository).save(inventory);
    }

    public <T> InventoryItemDto.InventoryItemDtoListWithPriority getItems(T target, int page, List<ItemType> priorityTypes) {
        InventoryRepositoryCustom<T> repository = this.getRepository(target);

        List<InventoryItemDto> priorityDtoList = Collections.emptyList();
        if (!priorityTypes.isEmpty()) {
            priorityDtoList = repository.getItems(target, priorityTypes);
        }

        List<InventoryItemDto> dtoList = repository.getItems(target, page);
        return new InventoryItemDto.InventoryItemDtoListWithPriority(priorityDtoList, dtoList);
    }

    public int getItemCount(User user) {
        return this.getRepository(user).getItemCount(user);
    }

    public <T> Integer getItemCount(T target, ItemType itemType) {
        InventoryRepositoryCustom<T> repository = this.getRepository(target);
        return repository.getItemCount(target, itemType);
    }

    public <T> int addItem(T target, ItemType itemType, int count) {
        InventoryRepositoryCustom<T> repository = this.getRepository(target);
        InventoryItemDto itemDto = repository.getItem(target, itemType);

        if (itemDto == null) {
            this.create(target, itemType, count);
            return count;
        } else if (count == 0) {
            return itemDto.count();
        }

        int newCount = itemDto.count() + count;
        repository.setCount(target, itemType, newCount);

        return newCount;
    }

    public <T> void setItem(T target, ItemType itemType, int count) {
        this.getRepository(target).setCount(target, itemType, count);
    }
}
