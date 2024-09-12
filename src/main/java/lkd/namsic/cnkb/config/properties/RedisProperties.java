package lkd.namsic.cnkb.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.redis")
public record RedisProperties(
    String host,
    String username,
    String password,
    Integer port,
    Integer database,
    Boolean ssl,
    String prefix
) {
}