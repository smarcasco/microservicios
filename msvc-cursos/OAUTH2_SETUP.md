# Configuración OAuth2 para msvc-cursos

Este fichero muestra cómo agregar OAuth2 Client a **msvc-cursos** (puerto 8002).

## 1. Agregar Dependencia en pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

## 2. Agregar Configuración en application.properties

```properties
# Configuración OAuth2 Client
spring.security.oauth2.client.registration.cursos-client.client-id=cursos-client
spring.security.oauth2.client.registration.cursos-client.client-secret=cursos-secret-123
spring.security.oauth2.client.registration.cursos-client.client-name=Cursos Service
spring.security.oauth2.client.registration.cursos-client.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.cursos-client.redirect-uri=http://localhost:8002/login/oauth2/code/cursos-client
spring.security.oauth2.client.registration.cursos-client.scope=openid,profile,read,write

# OAuth2 Provider Configuration
spring.security.oauth2.client.provider.cursos-client.issuer-uri=http://localhost:9000/auth
spring.security.oauth2.client.provider.cursos-client.authorization-uri=http://localhost:9000/auth/oauth2/authorize
spring.security.oauth2.client.provider.cursos-client.token-uri=http://localhost:9000/auth/oauth2/token
spring.security.oauth2.client.provider.cursos-client.user-info-uri=http://localhost:9000/auth/userinfo
spring.security.oauth2.client.provider.cursos-client.jwk-set-uri=http://localhost:9000/auth/oauth2/jwks
```

## 3. Crear SecurityConfig.java en msvc-cursos

```java
package com.sergio.microservicios.cursos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(withDefaults())
            .logout(withDefaults());

        return http.build();
    }
}
```

## 4. Puntos Importantes

- **clientId**: `cursos-client` (debe coincider con el registrado en msvc-auth)
- **clientSecret**: `cursos-secret-123` (debe coincider con el de SecurityConfig en msvc-auth)
- **redirectUri**: `http://localhost:8002/login/oauth2/code/cursos-client`
- **issuer-uri**: `http://localhost:9000/auth` (URL del Auth Server)
- **Puerto**: `8002` (verifica que sea el correcto)

## 5. Verificación

Una vez configurado:

1. Inicia msvc-auth en puerto 9000
2. Inicia msvc-cursos en puerto 8002
3. Accede a: `http://localhost:8002/`
4. Serás redirigido a `http://localhost:9000/auth/login`
5. Usa credenciales: `admin` / `admin123`
6. Autoriza la aplicación
7. Serás redirigido a `http://localhost:8002` autenticado con OAuth2

