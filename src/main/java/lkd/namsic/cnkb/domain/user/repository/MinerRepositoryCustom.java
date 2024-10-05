package lkd.namsic.cnkb.domain.user.repository;

import lkd.namsic.cnkb.domain.user.Miner;

import java.time.LocalDateTime;

public interface MinerRepositoryCustom {

    void updateCheckedAt(Miner targetMiner, LocalDateTime localDateTime);
}
