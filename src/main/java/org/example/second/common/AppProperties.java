package org.example.second.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app") //prefix 의 app 은 application.yaml 파일의 34Line 의 app
public class AppProperties {
    private final Jwt jwt = new Jwt();
    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private String headerSchemaName;
        private String tokenType;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
        private int refreshTokenCookieMaxAge;

        public void setRefreshTokenExpiry(long refreshTokenExpiry) {
            this.refreshTokenExpiry = refreshTokenExpiry;
            this.refreshTokenCookieMaxAge = (int)(refreshTokenExpiry * 0.001) ; // ms > s 변환
        }
    }
}
