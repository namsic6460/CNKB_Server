package lkd.namsic.cnkb.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lkd.namsic.cnkb.domain.user.Miner;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lkd.namsic.cnkb.domain.user.QMiner.miner;

@RequiredArgsConstructor
public class MinerRepositoryImpl implements MinerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void updateCheckedAt(Miner targetMiner, LocalDateTime localDateTime) {
        this.queryFactory.update(miner)
            .where(miner.eq(targetMiner))
            .set(miner.checkedAt, localDateTime)
            .execute();
    }
}
