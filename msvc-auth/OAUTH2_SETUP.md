# Configuración OAuth2 - Adaptación para Microservicios

## Resumen de Cambios

El fichero `SecurityConfig.java` ha sido adaptado para la arquitectura de microservicios con los siguientes cambios:

### 1. **Clientes OAuth2 Registrados**

Se han configurado **3 clientes OAuth2**, uno para cada microservicio:

| Cliente | ID | Puerto | Endpoint |
|---------|----|----|----------|
| **Usuarios** | `usuarios-client` | 8001 | `http://localhost:8001/login/oauth2/code/usuarios-client` |
| **Cursos** | `cursos-client` | 8002 | `http://localhost:8002/login/oauth2/code/cursos-client` |
| **Gateway** | `gateway-client` | 8080 | `http://localhost:8080/login/oauth2/code/gateway-client` |

### 2. **Configuración del Auth Server**

- **Puerto**: 9000
- **Context Path**: `/auth`
- **URL Base**: `http://localhost:9000/auth`

### 3. **Credenciales por Defecto**

**Usuario Administrativo:**
- Username: `admin`
- Password: `admin123`
- Rol: `ADMIN`

### 4. **Credenciales de Clientes OAuth2**

Cada cliente tiene un `clientSecret` específico:
- `usuarios-client`: `usuarios-secret-123`
- `cursos-client`: `cursos-secret-123`
- `gateway-client`: `gateway-secret-123`

## Ficheros Modificados

### `msvc-auth/src/main/resources/application.properties`
```properties
spring.application.name=msvc-auth
server.port=9000
server.servlet.context-path=/auth
```

### `msvc-auth/src/main/java/com/sergio/msvc/springcloud/SecurityConfig.java`

Cambios principales:
- ✅ Eliminada la clase antigua con un solo cliente
- ✅ Añadidos 3 clientes OAuth2 configurados
- ✅ Cada cliente tiene su propio `clientId` y `clientSecret`
- ✅ Configurados `redirectUri` específicos para cada servicio
- ✅ Habilitados scopes: `openid`, `profile`, `read`, `write`
- ✅ Deshabilitado `requireAuthorizationConsent` para evitar diálogos innecesarios

## Configuración Necesaria en los Microservicios

Para que cada microservicio funcione con OAuth2, necesitas agregar en sus `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

Y en sus `application.properties`:

```properties
# OAuth2 Client Configuration
spring.security.oauth2.client.registration.{nombre-cliente}.client-id={client-id}
spring.security.oauth2.client.registration.{nombre-cliente}.client-secret={client-secret}
spring.security.oauth2.client.registration.{nombre-cliente}.redirect-uri=http://localhost:{puerto}/login/oauth2/code/{nombre-cliente}
spring.security.oauth2.client.provider.{nombre-cliente}.issuer-uri=http://localhost:9000/auth
```

**Ejemplo para `msvc-usuarios`:**

```properties
# OAuth2 Client Configuration
spring.security.oauth2.client.registration.usuarios-client.client-id=usuarios-client
spring.security.oauth2.client.registration.usuarios-client.client-secret=usuarios-secret-123
spring.security.oauth2.client.registration.usuarios-client.redirect-uri=http://localhost:8001/login/oauth2/code/usuarios-client
spring.security.oauth2.client.provider.usuarios-client.issuer-uri=http://localhost:9000/auth
```

## Endpoints OAuth2 Disponibles

| Endpoint | URL |
|----------|-----|
| Authorization | `http://localhost:9000/auth/oauth2/authorize` |
| Token | `http://localhost:9000/auth/oauth2/token` |
| Token Revocation | `http://localhost:9000/auth/oauth2/revoke` |
| OIDC Userinfo | `http://localhost:9000/auth/userinfo` |
| OIDC Discovery | `http://localhost:9000/auth/.well-known/openid-configuration` |
| JWKS | `http://localhost:9000/auth/oauth2/jwks` |

## Próximos Pasos

1. Configurar cada microservicio como OAuth2 Client
2. Ejecutar el Auth Server: `java -jar msvc-auth-0.0.1-SNAPSHOT.jar`
3. Probar flujo de login OAuth2 desde cada servicio

## Notas de Seguridad

⚠️ **IMPORTANTE**: Esta configuración es de desarrollo. Para producción:
- Cambiar `{noop}` por contraseñas hasheadas con bcrypt
- Usar HTTPS en lugar de HTTP
- Almacenar credenciales en variables de entorno
- Usar base de datos en lugar de `InMemoryRegisteredClientRepository`

