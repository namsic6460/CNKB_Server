package lkd.namsic.cnkb.domain.item;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lkd.namsic.cnkb.config.converter.ItemTypeConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.enums.ItemType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_itemType", columnList = "item_type")
})
public class Item extends AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = ItemTypeConverter.class)
    @Column(length = 31, nullable = false, unique = true)
    private ItemType itemType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String acquireWay;

    @OneToMany(mappedBy = "key.item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<UserInventory> userInventories = new ArrayList<>();

    @OneToMany(mappedBy = "key.item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<MapLimitItem> mapLimitItems = new ArrayList<>();

    public static Item create(ItemType itemType, String description, String acquireWay) {
        Item item = new Item();
        item.itemType = itemType;
        item.description = description;
        item.acquireWay = acquireWay;

        return item;
    }
}
