package lkd.namsic.cnkb.domain.map;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lkd.namsic.cnkb.domain.AbstractEntity;
import lkd.namsic.cnkb.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "passed_maps")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassedMap extends AbstractEntity {

    @EmbeddedId
    private PassedMapKey key;

    public static PassedMap create(GameMap map, User user) {
        PassedMap passedMap = new PassedMap();
        passedMap.key = PassedMapKey.create(map, user);

        return passedMap;
    }
}
