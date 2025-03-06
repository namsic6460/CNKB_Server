package lkd.namsic.cnkb.domain.item;

public interface Inventory<T> {

    InventoryKey<T> getKey();
    int getCount();
}
