package lkd.namsic.cnkb.domain.map.repository;

import lkd.namsic.cnkb.domain.map.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameMapRepository extends JpaRepository<GameMap, Long>, GameMapRepositoryCustom {
}
