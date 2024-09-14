package lkd.namsic.cnkb.domain.user;

import jakarta.persistence.*;
import lkd.namsic.cnkb.config.converter.StringListConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.item.UserInventory;
import lkd.namsic.cnkb.domain.npc.Chat;
import lkd.namsic.cnkb.enums.ActionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @Getter(AccessLevel.PRIVATE)
    private ActionType actionType;

    @Setter
    @JoinColumn(name = "chat_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Chat chat;

    @Column
    @Convert(converter = StringListConverter.class)
    private final List<String> titleList = new ArrayList<>();

    @OneToMany(mappedBy = "key.user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<UserInventory> userInventoryList = new ArrayList<>();

    public static User create(long id, String name) {
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
        user.getTitleList().add(user.title);

        return user;
    }
}
