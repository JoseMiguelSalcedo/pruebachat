# pruebachat — Planeador de fin de semana multiagente (Spring AI + Ollama)

Sistema multiagente construido con Spring Boot 4.1, Spring AI 2.0 y Ollama (`qwen2.5:3b`) en la rama `taller-agentes`.

## Arquitectura
- **Supervisor** (`Decision` con salida estructurada JSON nativa)
- **Especialistas con `@Tool`:** `ClimaTools`, `PresupuestoTools`, `ReservasTools` y `ActividadesTools` (reto Parte 7)
- **Orquestador:** `PlaneadorOrquestador` (patrón orchestrator-workers, `MAX_PASOS = 3`)
- **Integración:** `AgentesModelService` (`@Primary`) detrás de `ModelService`, sin modificar `ChatController` ni el frontend.

## Ejecución con Docker
    git checkout taller-agentes
    docker compose -f docker/docker-compose.yml up -d --build
    docker logs -f pruebachat_backend

- Frontend: http://localhost:4200
- Depuración de agentes: http://localhost:8080/api/v1/debug/{clima|presupuesto|reservas|actividades}?q=...

## Ejecución local (sin Docker, con Ollama + JDK 21 + Gradle)
    ollama pull qwen2.5:3b
    cd backend
    gradle test
    gradle bootRun --args="--spring.web.resources.static-locations=file:../frontend/src/"

- Frontend local: http://localhost:8080/index.html

## Documentación
La bitácora del grupo en PDF se encuentra en `docs/`.