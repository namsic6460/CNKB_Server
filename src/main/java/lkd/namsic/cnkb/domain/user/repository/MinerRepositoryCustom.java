package lkd.namsic.cnkb.domain.user.repository;

import lkd.namsic.cnkb.domain.user.Miner;

import java.time.LocalDateTime;

public interface MinerRepositoryCustom {

    void updateCheckedAt(Miner targetMiner, LocalDateTime localDateTime);
    void updateSpeedLv(Miner targetMiner, int speedLv);
    void updateQualityLv(Miner targetMiner, int speedLv);
    void updateStorageLv(Miner targetMiner, int speedLv);
}
