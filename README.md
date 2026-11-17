# ms-tienda — Microservicio de Productos y Ventas

Microservicio REST desarrollado con **Spring Boot** para la gestión de una tienda. Está organizado en dos módulos independientes:

- **Productos**: alta, consulta, búsqueda, actualización y baja de productos.
- **Ventas**: registro de ventas con varios productos, validación y descuento de stock, historial y cancelación (con devolución de stock).

Frontend que consume este servicio: [frontend-lezkor](https://github.com/AngelGabrielAntonioMendez/frontend-lezkor)

## Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 17+ |
| Spring Boot | 4.1.2 |
| Spring Web MVC | REST API |
| Spring Data JPA / Hibernate | Persistencia |
| Bean Validation | Validación de datos |
| MySQL | 8.0 |
| Maven | Wrapper incluido (`mvnw`) |
| JUnit 5 + Mockito + MockMvc | Pruebas |

## Arquitectura

```
[ Frontend Vue :5173 ] --HTTP/JSON--> [ ms-tienda :8080 ] --JDBC--> [ MySQL :3307 ]
```

Cada módulo sigue una arquitectura por capas:

```
Controller  →  Service  →  Repository  →  Entity (JPA)
```

## Estructura del proyecto

```
src/main/java/com/tienda/ms_tienda/
├── MsTiendaApplication.java
├── comun/
│   ├── config/CorsConfig.java              # Permite peticiones desde el frontend
│   └── exception/                          # Manejo global de errores (JSON)
├── productos/
│   ├── controller/  service/  repository/  entity/
└── ventas/
    ├── controller/  service/  repository/  entity/
    └── dto/                                # VentaRequest, ItemVentaRequest
```

## Requisitos previos

- JDK 17 o superior
- MySQL 8.0
- No necesitas instalar Maven: el proyecto incluye `mvnw`

## Instalación y ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/AngelGabrielAntonioMendez/backend-ms-tienda.git
cd backend-ms-tienda
```

### 2. Crear la base de datos
Ejecuta el script [`db/tienda_db.sql`](db/tienda_db.sql) en MySQL Workbench.
Crea la base `tienda_db` con las tablas `productos`, `ventas` y `detalle_venta`, y agrega 4 productos de ejemplo.

### 3. Configurar la conexión
Revisa `src/main/resources/application.properties` y ajusta el puerto y el usuario de tu MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/tienda_db?...
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

> La contraseña **no se guarda en el repositorio**: se lee de la variable de entorno `DB_PASSWORD`.

### 4. Ejecutar

Windows (PowerShell):
```powershell
$env:DB_PASSWORD="tu_contraseña"
.\mvnw.cmd spring-boot:run
```

Linux / macOS:
```bash
export DB_PASSWORD="tu_contraseña"
./mvnw spring-boot:run
```

El servicio queda disponible en **http://localhost:8080**.

## Endpoints

### Productos — `/api/productos`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/productos` | Lista todos los productos | 200 |
| GET | `/api/productos?nombre=lap` | Busca por nombre | 200 |
| GET | `/api/productos/{id}` | Obtiene un producto | 200 / 404 |
| POST | `/api/productos` | Crea un producto | 201 / 400 |
| PUT | `/api/productos/{id}` | Actualiza un producto | 200 / 400 / 404 |
| DELETE | `/api/productos/{id}` | Elimina un producto | 204 / 404 / 409 |

### Ventas — `/api/ventas`

| Método | Endpoint | Descripción | Respuesta |
|---|---|---|---|
| GET | `/api/ventas` | Historial de ventas con detalles | 200 |
| GET | `/api/ventas/{id}` | Obtiene una venta | 200 / 404 |
| POST | `/api/ventas` | Registra una venta | 201 / 400 / 404 |
| DELETE | `/api/ventas/{id}` | Cancela una venta y devuelve el stock | 204 / 404 |

### Ejemplos

**Crear producto**
```json
POST /api/productos
{
  "nombre": "Audífonos HyperX",
  "descripcion": "Cloud II",
  "precio": 1599.00,
  "stock": 20
}
```

**Registrar venta**: el cliente solo envía ids y cantidades; **precios y total los calcula el servidor**.
```json
POST /api/ventas
{
  "cliente": "Ana López",
  "items": [
    { "productoId": 1, "cantidad": 1 },
    { "productoId": 2, "cantidad": 3 }
  ]
}
```

### Errores
Todos los errores se responden con el mismo formato:
```json
{ "mensaje": "Stock insuficiente para 'Monitor Samsung'. Disponible: 8" }
```

| Código | Cuándo ocurre |
|---|---|
| 400 | Datos inválidos o stock insuficiente |
| 404 | El producto o la venta no existe |
| 409 | Se intenta eliminar un producto que ya tiene ventas |

## Reglas de negocio

- No se puede vender más unidades de las que hay en stock.
- El precio de cada renglón se guarda al momento de la venta (registro histórico).
- Registrar una venta es **transaccional**: si un producto falla, no se guarda nada.
- Cancelar una venta devuelve las unidades al inventario.

## Pruebas

```powershell
.\mvnw.cmd test
```

| Clase | Tipo |
|---|---|
| `ProductoControllerTest` | Capa web (`@WebMvcTest`) |
| `VentaServiceTest` | Unitaria (Mockito) |
| `VentaIntegrationTest` | Integración con MySQL (con rollback) |

## Autor

**Antonio Mendez Angel Gabriel**
Ingeniería en Sistemas Computacionales — Instituto Tecnológico de Oaxaca
Materia: Desarrollo de Servicios Web
