package lkd.namsic.cnkb.domain.npc;

import jakarta.persistence.*;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.enums.NpcType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Npc extends AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 31, nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private GameMap gameMap;

    @OneToMany(mappedBy = "npc", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Chat> chats = new ArrayList<>();

    public static Npc create(NpcType npcType, GameMap gameMap) {
        Npc npc = new Npc();
        npc.name = npcType.getValue();
        npc.gameMap = gameMap;

        return npc;
    }

    public void addChat(Chat chat) {
        if (!this.chats.contains(chat)) {
            this.chats.add(chat);
        }
    }
}
