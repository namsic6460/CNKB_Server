package lkd.namsic.cnkb.domain.item;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public abstract class InventoryKey<T> implements Serializable {

    public abstract T getTarget();
    public abstract Item getItem();
}
