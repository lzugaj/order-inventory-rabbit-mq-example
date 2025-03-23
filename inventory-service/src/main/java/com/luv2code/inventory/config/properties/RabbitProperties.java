package com.luv2code.inventory.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitProperties {

    private String host;
    private int port;
    private String username;
    private String password;

}
