package lkd.namsic.cnkb.domain.user.repository;

import lkd.namsic.cnkb.domain.user.Miner;
import lkd.namsic.cnkb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinerRepository extends JpaRepository<Miner, User>, MinerRepositoryCustom {
}
