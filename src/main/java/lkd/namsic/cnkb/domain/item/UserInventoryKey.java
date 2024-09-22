package lkd.namsic.cnkb.domain.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lkd.namsic.cnkb.domain.user.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInventoryKey extends InventoryKey<User> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public static UserInventoryKey create(User user, Item item) {
        UserInventoryKey key = new UserInventoryKey();
        key.user = user;
        key.item = item;

        return key;
    }

    @Override
    public User getTarget() {
        return this.user;
    }
}
