package lkd.namsic.cnkb.domain.npc;

import jakarta.persistence.*;
import lkd.namsic.cnkb.domain.AbstractEntity;
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

    @Column(length = 15, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "npc", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Chat> chatList = new ArrayList<>();

    public static Npc create(NpcType npcType) {
        Npc npc = new Npc();
        npc.name = npcType.getValue();

        return npc;
    }

    public void addChat(Chat chat) {
        if (!this.chatList.contains(chat)) {
            this.chatList.add(chat);
        }
    }
}
