# Sistema Logistico: Evaluacion de Patrones de Integracion

Este repositorio contiene la solucion completa para la evaluacion practica de Patrones de Integracion. Se ha implementado un sistema bajo una arquitectura hibrida de microservicios, combinando patrones de Orquestacion (Sincrona) y Coreografia (Asincrona).

## Arquitectura y Patrones Aplicados

El proyecto esta disenado bajo una estrategia de **Monorepo** utilizando Maven Multi-Modulo, garantizando la independencia funcional de cada dominio.

1. **Database per Service (Aislamiento de BD)**
   - Cada microservicio critico posee su propia base de datos fisica independiente (PostgreSQL en puertos distintos), evitando el acoplamiento a nivel de datos.
2. **Orquestacion (Integracion Sincrona - HTTP/REST)**
   - El `envio-service` actua como el orquestador principal.
   - Consume via REST al `tarifa-service` para obtener el costo del envio de forma bloqueante, garantizando el control del flujo critico y la consistencia transaccional.
3. **Coreografia (Integracion Asincrona - Event-Driven)**
   - El `notificacion-service` es un suscriptor autonomo.
   - Utilizando **RabbitMQ (AMQP)** como Message Broker, el orquestador delega la tarea de notificar al usuario emitiendo el evento `EnvioCreado` en estricto formato **JSON**. Esto garantiza alta disponibilidad y desacoplamiento total.

## Tecnologias Utilizadas

- Java 17 y Spring Boot 3
- RabbitMQ (Message Broker)
- PostgreSQL (Bases de datos independientes)
- Docker y Docker Compose (Gestion de Infraestructura)
- REST y Jackson (Serializacion JSON)

## Como ejecutar el proyecto localmente

### 1. Levantar la Infraestructura (Docker)
Navega a la raiz del proyecto y ejecuta el archivo `docker-compose.yml`:
```bash
docker-compose up -d
```
Esto levantara RabbitMQ (puerto 5672) y las dos instancias de PostgreSQL (puertos 5432 y 5433).

### 2. Compilar y Ejecutar Microservicios
Abre el proyecto en IntelliJ IDEA, recarga los proyectos Maven e inicia las aplicaciones en el siguiente orden:
- `TarifaServiceApplication` (Puerto 8082)
- `NotificacionServiceApplication` (Sin puerto web)
- `EnvioServiceApplication` (Puerto 8081)

## Realizar una prueba (Creacion de Envio)

Realiza un `POST` al orquestador en `http://localhost:8081/api/envios` con el siguiente cuerpo JSON:

```json
{
    "clienteId": "CLI-12345",
    "peso": 5.5,
    "destino": "Internacional"
}
```
Podras observar en las consolas como se realiza el calculo sincrono primero, para luego liberar al cliente y procesar la notificacion en segundo plano a traves de la cola de RabbitMQ.
