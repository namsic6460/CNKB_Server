package lkd.namsic.cnkb.domain.npc;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import lkd.namsic.cnkb.config.converter.ReplyTypeLongMapConverter;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.enums.NamedChat;
import lkd.namsic.cnkb.enums.ReplyType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Entity(name = "chats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_namedChat", columnList = "namedChat")
})
public class Chat extends AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 31)
    private NamedChat namedChat;

    @Column(nullable = false, length = 127)
    private String text;

    @Builder.Default
    @Column(nullable = false)
    private Long delay = 2000L;

    @Column
    @Builder.Default
    private boolean isForce = false;

    @Column
    @Builder.Default
    private boolean isFirst = false;

    @Column
    @Builder.Default
    private boolean isConnected = true;

    @JoinColumn(name = "npc_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Npc npc;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_chat_id")
    private Chat nextChat;

    @Column
    @Convert(converter = ReplyTypeLongMapConverter.class)
    private final Map<ReplyType, Long> availableRepliyMap = new HashMap<>();

    public void setNpc(Npc npc) {
        this.npc = npc;

        if (npc != null && !npc.getChats().contains(this)) {
            npc.addChat(this);
        }
    }
}
