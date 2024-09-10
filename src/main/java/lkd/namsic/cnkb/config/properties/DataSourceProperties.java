package lkd.namsic.cnkb.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "data-source")
public record DataSourceProperties(
    String username,
    String password,
    String url
) {
}