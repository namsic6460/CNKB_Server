package lkd.namsic.cnkb.domain.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lkd.namsic.cnkb.config.converter.EnumListConverter;
import lkd.namsic.cnkb.config.converter.StatTypeLongMapConverter;
import lkd.namsic.cnkb.config.converter.StringListConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.item.UserInventory;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.enums.ActionType;
import lkd.namsic.cnkb.enums.ItemType;
import lkd.namsic.cnkb.enums.StatType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractEntity {

    @Id
    @Column
    private Long id;

    @Column(nullable = false, length = 15, unique = true)
    private String name;

    @Column(nullable = false)
    private int lv;

    @Column(nullable = false)
    private long exp;

    @Column(nullable = false)
    private int sp;

    @Column(nullable = false)
    private long money;

    @Column(nullable = false)
    private int maxHp;

    @Column(nullable = false)
    private int hp;

    @Column(nullable = false)
    private int maxMn;

    @Column(nullable = false)
    private int mn;

    @Column
    private String title;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Setter
    @JoinColumn(name = "chat_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private GameMap currentGameMap;

    @OneToOne(mappedBy = "user")
    private Miner miner;

    @Column
    @Convert(converter = StringListConverter.class)
    private final List<String> titles = new ArrayList<>();

    @Column(length = 320) // Item 클래스 기준 ItemType max length 31 * 10(최대 설정 개수) + 구분자(;) 9개 + 1(깔끔하게)
    @Convert(converter = EnumListConverter.ItemTypeListConverter.class)
    private final List<ItemType> priorityItemTypes = new ArrayList<>();

    @Column
    @Convert(converter = StatTypeLongMapConverter.class)
    private final Map<StatType, Long> stat = new EnumMap<>(StatType.DEFAULT_STAT);

    @OneToMany(mappedBy = "key.user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<UserInventory> inventory = new ArrayList<>();

    public static User create(long id, String name, GameMap startVillage) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.lv = 1;
        user.exp = 0;
        user.sp = 5;
        user.money = 0;
        user.maxHp = 1000;
        user.hp = user.maxHp;
        user.maxMn = 100;
        user.mn = user.maxMn;
        user.title = "초심자";
        user.actionType = ActionType.FORCE_CHAT;
        user.currentGameMap = startVillage;
        user.miner = Miner.create(user);
        user.getTitles().add(user.title);

        return user;
    }
}
