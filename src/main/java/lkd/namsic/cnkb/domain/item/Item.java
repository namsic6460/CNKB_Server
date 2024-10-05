package lkd.namsic.cnkb.domain.item;

import jakarta.persistence.*;
import lkd.namsic.cnkb.config.converter.ItemTypeConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.enums.ItemType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Item create(ItemType itemType, String description, String acquireWay) {
        Item item = new Item();
        item.itemType = itemType;
        item.description = description;
        item.acquireWay = acquireWay;

        return item;
    }
}
