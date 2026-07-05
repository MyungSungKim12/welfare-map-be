package com.welfareMap.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {

    private String cookieName = "WM_ACCESS_TOKEN";
    private long cookieMaxAgeSeconds = 3600;
    private boolean cookieSecure = false;
}
