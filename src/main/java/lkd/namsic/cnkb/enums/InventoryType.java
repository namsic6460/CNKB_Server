package lkd.namsic.cnkb.enums;

import java.util.HashMap;
import java.util.Map;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.user.User;
import lombok.Getter;

@Getter
public enum InventoryType {

    USER(User.class),
    MAP(GameMap.class)
    ;

    private static final Map<Class<?>, InventoryType> classMap = new HashMap<>();
    private final Class<?> c;

    static {
        for (InventoryType inventoryType : InventoryType.values()) {
            classMap.put(inventoryType.c, inventoryType);
        }
    }

    public static <T> InventoryType find(T target) {
        return classMap.get(target.getClass());
    }

    InventoryType(Class<?> c) {
        this.c = c;
    }
}
