package lkd.namsic.cnkb.domain.npc;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.map.GameMap;
import lkd.namsic.cnkb.enums.domain.NpcType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Npc extends AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 31, nullable = false, unique = true)
    private NpcType npcType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private GameMap gameMap;

    @OneToMany(mappedBy = "npc", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Chat> chats = new ArrayList<>();

    public static Npc create(NpcType npcType, GameMap gameMap) {
        Npc npc = new Npc();
        npc.npcType = npcType;
        npc.gameMap = gameMap;

        return npc;
    }

    public void addChat(Chat chat) {
        if (!this.chats.contains(chat)) {
            this.chats.add(chat);
        }
    }
}
