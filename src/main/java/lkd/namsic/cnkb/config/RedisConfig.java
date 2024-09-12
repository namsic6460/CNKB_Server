package lkd.namsic.cnkb.config;

import lkd.namsic.cnkb.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(this.redisProperties.host());
        config.setPort(this.redisProperties.port());
        config.setDatabase(this.redisProperties.database());
        config.setUsername(this.redisProperties.username());
        config.setPassword(this.redisProperties.password());

        return new LettuceConnectionFactory(config);
    }
}
