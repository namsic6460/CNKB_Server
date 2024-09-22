package lkd.namsic.cnkb.domain.item.repository;

import lkd.namsic.cnkb.domain.item.UserInventory;
import lkd.namsic.cnkb.domain.item.UserInventoryKey;
import lkd.namsic.cnkb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInventoryRepository extends JpaRepository<UserInventory, UserInventoryKey>, InventoryRepositoryCustom<User> {
}
