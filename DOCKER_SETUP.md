# Microservicios - Docker Setup

## Ejecución con Docker

### Opción 1: Ejecutar solo msvc-usuarios

```bash
cd msvc-usuarios
docker build -t msvc-usuarios:latest .
docker run -p 8001:8001 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3307/msvc_usuarios?useSSL=false\&serverTimezone=UTC\&allowPublicKeyRetrieval=true \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  msvc-usuarios:latest
```

El `Dockerfile` de `msvc-usuarios` ya usa el archivo `msvc-usuarios/.m2/settings.xml`, por lo que el build funciona desde el propio módulo y no depende del `pom.xml` padre de la raíz.

### Opción 2: Ejecutar con Docker Compose (Recomendado)

Ejecutar todos los servicios (MySQL, msvc-usuarios, PostgreSQL, msvc-cursos):

```bash
# Desde la raíz del proyecto
docker-compose up -d
```

Ver logs:
```bash
docker-compose logs -f msvc-usuarios
```

Detener servicios:
```bash
docker-compose down
```

Eliminar contenedores (los datos se conservan en volúmenes):
```bash
docker-compose down
```

Eliminar contenedores Y volúmenes (borra todos los datos):
```bash
docker-compose down -v
```

## Configuración de Puertos

- **msvc-usuarios**: 8001
- **msvc-cursos**: 8002
- **MySQL**: 3307
- **PostgreSQL**: 5432

## Persistencia de datos

Los datos se persisten en volúmenes nombrados gestionados por Docker:

- **MySQL**: volumen `mysql-data`
- **PostgreSQL**: volumen `postgres-data`

Los datos **no se pierden** al hacer `docker compose down`. Solo se borran con `docker compose down -v`.

Para inspeccionar los volúmenes:
```bash
docker volume ls
docker volume inspect microservicios_mysql-data
docker volume inspect microservicios_postgres-data
```

## Variables de Entorno

### msvc-usuarios
- `SPRING_DATASOURCE_URL`: jdbc:mysql://mysql:3306/msvc_usuarios?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
- `SPRING_DATASOURCE_USERNAME`: root
- `SPRING_DATASOURCE_PASSWORD`: 123456
- `CLIENTS_CURSOS_URL`: http://msvc-cursos:8002
- `JAVA_OPTS`: -Xmx512m -Xms256m

### msvc-cursos
- `SPRING_DATASOURCE_URL`: jdbc:postgresql://postgresql:5432/msvc_cursos
- `SPRING_DATASOURCE_USERNAME`: postgres
- `SPRING_DATASOURCE_PASSWORD`: 123456
- `CLIENTS_USUARIOS_URL`: http://msvc-usuarios:8001
- `JAVA_OPTS`: -Xmx512m -Xms256m

## Verificar que los servicios están corriendo

```bash
# Verificar msvc-usuarios
curl http://localhost:8001/

# Verificar msvc-cursos
curl http://localhost:8002/
```

## Limpiar images y contenedores

```bash
docker system prune -a
```

