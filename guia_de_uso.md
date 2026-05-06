# Guía de Uso y Pruebas del Sistema

Esta guía detalla los pasos para levantar, probar y comprobar el correcto funcionamiento de la arquitectura híbrida (Orquestación + Coreografía) desarrollada para el examen práctico, junto con la configuración de **Database per Service**.

## 🛠️ Paso 1: Levantar la Infraestructura (RabbitMQ + PostgreSQL)

El sistema requiere de bases de datos independientes para cumplir con el patrón "Database per Service", y utiliza **RabbitMQ** como Message Broker para implementar la comunicación asíncrona.

Todo esto está orquestado en el archivo `docker-compose.yml`.

Abre una terminal en la carpeta **`codigo_fuente/logistica-sistema`** y ejecuta:
```bash
docker-compose up -d
```
*(Esto descargará las imágenes y levantará 3 contenedores en segundo plano:)*
1. **RabbitMQ**: (Puertos 5672 y panel web en 15672).
2. **PostgreSQL Envío**: (Puerto 5432) Base de datos exclusiva para el microservicio de envíos.
3. **PostgreSQL Tarifa**: (Puerto 5433) Base de datos exclusiva para el microservicio de tarifas.

---

## 🚀 Paso 2: Arrancar los Microservicios en IntelliJ IDEA

Inicia los tres microservicios desde tu IDE. Asegúrate de hacer un **"Reload All Maven Projects"** previo. Ejecuta las clases principales:

1. **`TarifaServiceApplication`** (Puerto 8082).
   - Se conectará a su propia base de datos Postgres y creará automáticamente sus tablas.
2. **`EnvioServiceApplication`** (Puerto 8081).
   - Se conectará a RabbitMQ y a su propia base de datos Postgres de forma aislada.
3. **`NotificacionServiceApplication`** (Sin puerto web).
   - Se conecta únicamente a RabbitMQ. *(No tiene librerías ni base de datos, demostrando su autonomía).*

---

## 🧪 Paso 3: Probar el Sistema (Crear un Envío)

Simularemos la petición de un cliente enviando un **HTTP POST** al orquestador (`envio-service`).

### Usando Postman / Insomnia
- **Método**: `POST`
- **URL**: `http://localhost:8081/api/envios`
- **Headers**: `Content-Type: application/json`
- **Body (Raw JSON)**:
  ```json
  {
      "clienteId": "CLI-12345",
      "peso": 5.5,
      "destino": "Internacional"
  }
  ```

### Usando PowerShell
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/envios" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"clienteId": "CLI-12345", "peso": 5.5, "destino": "Internacional"}'
```

---

## 🔍 Paso 4: Comprobación Técnica para la Evaluación

Para sustentar el examen, demuestra visualmente la aplicación de los patrones requeridos:

### 1. Comprobar la Orquestación (Comunicación Síncrona)
* **Demostración:** Al ejecutar el POST, verás en la consola de `tarifa-service` el mensaje *"Calculando tarifa..."* y el cliente recibirá su respuesta HTTP 201 al instante con el costo final (`73.75`).
* **Prueba de Acoplamiento Fuerte:** Detén el `tarifa-service` en IntelliJ y vuelve a enviar el POST. La petición fallará inmediatamente. El orquestador *necesita* que el servicio síncrono esté vivo.

### 2. Comprobar la Coreografía (Comunicación Asíncrona)
* **Prueba de Resiliencia (Desacoplamiento):** Detén el `notificacion-service` en IntelliJ.
* Haz un POST a `envio-service` desde Postman. 
* ¡El flujo principal no se interrumpe! El cliente recibe su confirmación y el evento queda a salvo en el broker.

### 3. Comprobar el Broker de Mensajes (RabbitMQ)
1. Con el notificador apagado, entra en tu navegador a [http://localhost:15672](http://localhost:15672).
2. Usuario: **`guest`** | Contraseña: **`guest`**.
3. En la pestaña **"Queues"**, verás la cola `notificacion.queue` con **1 mensaje listo**.
4. Puedes inspeccionar el mensaje y demostrarle al evaluador que el evento está viajando en formato **JSON**, tal como lo pide la rúbrica.
5. Finalmente, enciende de nuevo el `notificacion-service` en IntelliJ. En un par de segundos consumirá la cola y verás la notificación asíncrona en consola.

### 4. Sustentación Final
**Patrón Database per Service:** Explícale a tu evaluador que el sistema usa PostgreSQL real, pero levantado en dos puertos físicos distintos (5432 y 5433) dentro de Docker, garantizando un aislamiento total de los datos (la autonomía que exige la arquitectura de Microservicios).
