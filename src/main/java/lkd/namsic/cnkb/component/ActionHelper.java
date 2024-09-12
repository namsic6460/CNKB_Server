package lkd.namsic.cnkb.component;

import lkd.namsic.cnkb.config.properties.RedisProperties;
import lkd.namsic.cnkb.domain.user.User;
import lkd.namsic.cnkb.domain.user.repository.UserRepository;
import lkd.namsic.cnkb.enums.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionHelper {

    private final RedisProperties redisProperties;
    private final RedisTemplate<String, String> redis;
    private final UserRepository userRepository;

    private final Duration timeout = Duration.of(1, ChronoUnit.HOURS);

    private String getKey(User user) {
        return this.redisProperties.prefix() +  "actionType:" + user.getId();
    }

    @Transactional(readOnly = true)
    public ActionType getActionType(User user) {
        ValueOperations<String, String> ops = this.redis.opsForValue();
        String key = this.getKey(user);

        String actionTypeString = ops.getAndExpire(key, this.timeout);
        if (actionTypeString != null) {
            log.info("ActionType from redis - {}", actionTypeString);
            return ActionType.valueOf(actionTypeString);
        }

        ActionType actionType = Objects.requireNonNull(this.userRepository.getActionType(user));
        ops.set(key, actionType.name(), this.timeout);
        log.info("ActionType from db - {}", actionType);

        return actionType;
    }

    @Transactional
    public void setActionType(User user, ActionType actionType) {
        ValueOperations<String, String> ops = this.redis.opsForValue();
        String key = this.getKey(user);

        ops.set(key, actionType.name(), this.timeout);
        this.userRepository.updateActionType(user, actionType);
        log.info("Set ActionType - {}", actionType);
    }
}
