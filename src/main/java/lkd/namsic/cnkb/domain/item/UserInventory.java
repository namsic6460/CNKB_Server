package lkd.namsic.cnkb.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "user_inventories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInventory extends AbstractEntity implements Inventory<User> {

    @EmbeddedId
    private UserInventoryKey key;

    @Column(nullable = false)
    private Integer count;

    public static UserInventory create(User user, Item item, Integer count) {
        UserInventory userInventory = new UserInventory();
        userInventory.key = UserInventoryKey.create(user, item);
        userInventory.count = count;

        return userInventory;
    }
}
