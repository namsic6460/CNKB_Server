package lkd.namsic.cnkb.config;

import lkd.namsic.cnkb.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.connection.SequentialDnsAddressResolverFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        String prefix = "redis";
        if (BooleanUtils.isTrue(this.redisProperties.ssl())) {
            prefix += "s";
        }

        Config config = new Config();
        config.setAddressResolverGroupFactory(new SequentialDnsAddressResolverFactory(1));
        config.useSingleServer()
            .setAddress(prefix + "://" + this.redisProperties.host() + ":" + this.redisProperties.port())
            .setUsername(this.redisProperties.username())
            .setPassword(this.redisProperties.password())
            .setDatabase(this.redisProperties.database());

        return Redisson.create(config);
    }
}