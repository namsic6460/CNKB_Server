package lkd.namsic.cnkb.domain.user;

import jakarta.persistence.*;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity(name = "miners")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Miner extends AbstractEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    private int lv;

    @Column(nullable = false)
    private LocalDateTime checkedAt;

    public static Miner create(User user) {
        Miner miner = new Miner();
        miner.user = user;
        miner.lv = 1;
        miner.checkedAt = LocalDateTime.now();

        return miner;
    }
}
