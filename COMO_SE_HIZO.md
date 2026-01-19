# Cómo se hizo: Sistema de Productos con Spring Boot

## 📋 Resumen
Este documento detalla todas las modificaciones realizadas para implementar un sistema REST de gestión de productos con relaciones entre Usuarios, Productos y Categorías usando Spring Boot, JPA/Hibernate y PostgreSQL.

---

## 🎯 Objetivo Principal
Implementar el endpoint `GET /api/users/{id}/products` que retorne todos los productos de un usuario específico, incluyendo la información del propietario (owner) y las categorías asociadas.

---

## 🐛 Problemas Encontrados y Soluciones

### 1. **Error de compilación: toEntity() con argumentos incorrectos**

**Problema:**
```java
// ❌ Error
ProductEntity entity = product.toEntity(owner, categoria);
// El método esperaba Set<CategoriaEntity> pero recibía CategoriaEntity
```

**Solución:**
```java
// ✅ Correcto
Set<CategoriaEntity> categorias = Set.of(categoria);
ProductEntity entity = product.toEntity(owner, categorias);
```

**Ubicación:** `ProductServiceImpl.java` - método `create()`

---

### 2. **Error: @OneToMany requiere colección, no objeto único**

**Problema:**
```java
// ❌ Error
@OneToMany(mappedBy = "owner")
private ProductEntity productEntity;
```

**Solución:**
```java
// ✅ Correcto
@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
private Set<ProductEntity> products;
```

**Ubicación:** `UserEntity.java`

**Explicación:** 
- `@OneToMany` indica que un usuario puede tener MUCHOS productos
- Por tanto, debe ser una colección (`Set`, `List`)
- `mappedBy = "owner"` indica que el campo `owner` en `ProductEntity` es el dueño de la relación

---

### 3. **NullPointerException: Comparación con id null**

**Problema:**
```java
// ❌ Error
if (this.id > 0) {
    entity.setId((long) this.id);
}
// Si id es null, lanza NullPointerException
```

**Solución:**
```java
// ✅ Correcto
if (this.id != null && this.id > 0) {
    entity.setId((long) this.id);
}
```

**Ubicación:** `User.java` y `Product.java` - métodos `toEntity()`

---

### 4. **Violación de constraint: user_id NULL en products**

**Problema:**
```sql
-- Error en BD
ERROR: null value in column "user_id" violates not-null constraint
```

**Causa:** El método `Product.toEntity()` no estaba asignando el owner

**Solución:**
```java
public ProductEntity toEntity(UserEntity owner, Set<CategoriaEntity> category) {
    ProductEntity entity = new ProductEntity();
    
    // ... otros campos ...
    
    // ✅ CRÍTICO: Asignar el owner
    entity.setOwner(owner);
    
    // ✅ Asignar categorías
    category.forEach(c -> entity.addCategorie(c));
    
    return entity;
}
```

**Ubicación:** `Product.java` - método `toEntity()`

---

### 5. **NullPointerException: Collections no inicializadas**

**Problema:**
```java
// ❌ Error
private Set<CategoriaEntity> categories;
// Al hacer categories.add() -> NullPointerException
```

**Solución:**
```java
// ✅ Correcto
private Set<CategoriaEntity> categories = new HashSet<>();
```

**Ubicación:** `ProductEntity.java`

**Explicación:** Las colecciones JPA deben inicializarse para evitar NPE al agregar elementos.

---

### 6. **User null en respuesta API**

**Problema:** Al crear un producto, el campo `user` retornaba null en el JSON.

**Causa:** `FetchType.LAZY` no cargaba la relación automáticamente.

**Solución:**
```java
// ✅ Cambiar a EAGER
@ManyToOne(optional = false, fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", nullable = false)
private UserEntity owner;
```

**Ubicación:** `ProductEntity.java`

**Explicación:**
- `LAZY`: Carga la relación solo cuando se accede explícitamente
- `EAGER`: Carga la relación inmediatamente con la consulta principal
- Para evitar lazy loading en transacciones cerradas, usamos EAGER para owner

---

### 7. **Categories y Owner NULL en GET /api/users/{id}/products**

**Problema:** Al obtener productos por usuario, `categories` y `user` retornaban null.

**Causa:** El modelo `Product` no tenía campos para almacenar estas relaciones cuando se creaba desde `ProductEntity`.

**Solución:**

```java
// 1. Agregar campos al modelo Product
public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private LocalDateTime createdAt;
    private Set<CategoriaEntity> categories;  // ✅ Nuevo
    private UserEntity owner;                  // ✅ Nuevo
}

// 2. Actualizar fromEntity() para copiar las relaciones
public static Product fromEntity(ProductEntity entity) {
    Product product = new Product(
        entity.getName(), 
        entity.getPrice(), 
        entity.getDescription()
    );
    product.id = entity.getId();
    product.categories = entity.getCategories();  // ✅ Copiar categories
    product.owner = entity.getOwner();            // ✅ Copiar owner
    return product;
}

// 3. Actualizar toResponseDto() para incluirlas en el DTO
public ProductResponseDto toResponseDto() {
    ProductResponseDto dto = new ProductResponseDto();
    dto.setId(this.id);
    dto.setName(this.name);
    dto.setDescription(this.description);
    dto.setPrice(this.price);
    dto.setStock(this.stock);
    
    // ✅ Agregar owner si existe
    if (this.owner != null) {
        ProductResponseDto.UserSummaryDto ownerDto = new ProductResponseDto.UserSummaryDto();
        ownerDto.id = this.owner.getId().intValue();
        ownerDto.name = this.owner.getName();
        dto.user = ownerDto;
    }
    
    // ✅ Agregar categories si existen
    if (this.categories != null && !this.categories.isEmpty()) {
        dto.categories = this.categories.stream()
            .map(cat -> {
                var categoryDto = new CategoriaResponseDto();
                categoryDto.id = cat.getId();
                categoryDto.name = cat.getName();
                return categoryDto;
            })
            .toList();
    }
    
    return dto;
}
```

**Ubicación:** `Product.java`

---

## 📁 Arquitectura del Código

### Estructura en Capas (Clean Architecture)

```
┌─────────────────────────────────────────────┐
│         CAPA DE PRESENTACIÓN                │
│  (Controllers - Endpoints REST)             │
│  - UsersController                          │
│  - ProductsController                       │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│         CAPA DE APLICACIÓN                  │
│  (Services - Lógica de negocio)             │
│  - UserService / UserServiceImpl            │
│  - ProductService / ProductServiceImpl      │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│         CAPA DE DOMINIO                     │
│  (Modelos de negocio)                       │
│  - User (model)                             │
│  - Product (model)                          │
└─────────────────────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│         CAPA DE DATOS                       │
│  (Entities, Repositories, DTOs)             │
│  - UserEntity / UserRepository              │
│  - ProductEntity / ProductRepository        │
│  - CategoriaEntity / CategoryRepository     │
└─────────────────────────────────────────────┘
```

---

## 🔄 Flujo de Datos

### Ejemplo: GET /api/users/{id}/products

```
1. REQUEST → UsersController.getProductsByUserId(Long id)
   ↓
2. UserService.getProdutsByUserId(userid)
   ↓
3. ProductRepository.findByOwnerId(userid)
   ↓  [Retorna List<ProductEntity>]
4. Stream → Product::fromEntity
   ↓  [Convierte ProductEntity → Product (modelo)]
5. Product::toResponseDto
   ↓  [Convierte Product → ProductResponseDto]
6. RESPONSE ← JSON con productos
```

**Código del flujo:**
```java
@GetMapping("/{id}/products")
public List<ProductResponseDto> getProductsByUserId(@PathVariable Long id) {
    return service.getProdutsByUserId(id);
}

// En UserServiceImpl
public List<ProductResponseDto> getProdutsByUserId(Long userid) {
    userRepo.findById(userid)
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    
    return productRepo.findByOwnerId(userid)
        .stream()
        .map(Product::fromEntity)        // Entity → Model
        .map(Product::toResponseDto)     // Model → DTO
        .toList();
}
```

---

## 🔗 Relaciones JPA

### User ↔ Product (One-to-Many / Many-to-One)

```java
// En UserEntity
@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
private Set<ProductEntity> products;

// En ProductEntity
@ManyToOne(optional = false, fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", nullable = false)
private UserEntity owner;
```

**Explicación:**
- Un usuario puede tener MUCHOS productos (`@OneToMany`)
- Un producto pertenece a UN SOLO usuario (`@ManyToOne`)
- `mappedBy = "owner"` indica que el lado Product es el dueño de la relación
- La columna `user_id` se crea en la tabla `products`

### Product ↔ Category (Many-to-Many)

```java
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "product_categories", 
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
)
private Set<CategoriaEntity> categories = new HashSet<>();
```

**Explicación:**
- Un producto puede tener MUCHAS categorías
- Una categoría puede estar en MUCHOS productos
- Se crea una tabla intermedia `product_categories` con dos columnas:
  - `product_id` (FK → products)
  - `category_id` (FK → categories)

---

## 🛠️ Métodos Auxiliares en ProductEntity

```java
public void addCategorie(CategoriaEntity categoria) {
    if (this.categories == null) {
        this.categories = new HashSet<>();
    }
    this.categories.add(categoria);
}
```

**Uso:**
```java
// En vez de hacer esto manualmente:
entity.getCategories().add(categoria);

// Usamos el método helper:
entity.addCategorie(categoria);
```

**Ventajas:**
- Inicializa la colección si es null
- Encapsula la lógica de agregar elementos
- Evita NullPointerExceptions

---

## 📝 DTOs (Data Transfer Objects)

### ¿Por qué usar DTOs?

1. **Seguridad:** No exponer campos sensibles (password, timestamps internos)
2. **Flexibilidad:** Estructura diferente a la entidad
3. **Versionado:** Cambios en API sin afectar BD
4. **Optimización:** Solo datos necesarios

### Ejemplo: ProductResponseDto

```java
public class ProductResponseDto {
    public Long id;
    public String name;
    public String description;
    public Double price;
    public Integer stock;
    public UserSummaryDto user;           // ✅ Resumen del owner
    public List<CategoriaResponseDto> categories;
    
    // Clase interna para resumir usuario
    public static class UserSummaryDto {
        public Integer id;
        public String name;
        // NO incluimos email, password, etc.
    }
}
```

---

## 🔍 FetchType: LAZY vs EAGER

### LAZY (Carga Perezosa)
```java
@OneToMany(fetch = FetchType.LAZY)
private Set<ProductEntity> products;
```

**Comportamiento:**
- NO carga los productos automáticamente
- Solo cuando se accede: `user.getProducts()`
- Genera consulta SQL adicional (N+1 problem)

**Ventaja:** Eficiencia si no siempre necesitas la relación  
**Desventaja:** LazyInitializationException si accedes fuera de transacción

### EAGER (Carga Anticipada)
```java
@ManyToOne(fetch = FetchType.EAGER)
private UserEntity owner;
```

**Comportamiento:**
- SIEMPRE carga el owner con el producto
- JOIN en la consulta SQL principal

**Ventaja:** No hay excepciones lazy loading  
**Desventaja:** Puede cargar datos innecesarios

### ¿Cuándo usar cada uno?

- **LAZY:** Relaciones @OneToMany, @ManyToMany grandes
- **EAGER:** Relaciones @ManyToOne, @OneToOne pequeñas y frecuentes

---

## 🚀 Endpoints Implementados

### 1. Crear Usuario
```http
POST /api/users
Content-Type: application/json

{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123"
}
```

### 2. Crear Producto
```http
POST /api/products
Content-Type: application/json

{
  "name": "Laptop Dell",
  "description": "Laptop Dell XPS 15",
  "price": 1200.00,
  "userId": 1,
  "categoryId": 1
}
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Laptop Dell",
  "description": "Laptop Dell XPS 15",
  "price": 1200.00,
  "stock": 0,
  "user": {
    "id": 1,
    "name": "Juan Pérez"
  },
  "categories": [
    {
      "id": 1,
      "name": "Electrónica"
    }
  ]
}
```

### 3. Obtener Productos de un Usuario
```http
GET /api/users/1/products
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Laptop Dell",
    "description": "Laptop Dell XPS 15",
    "price": 1200.00,
    "stock": 0,
    "user": {
      "id": 1,
      "name": "Juan Pérez"
    },
    "categories": [
      {
        "id": 1,
        "name": "Electrónica"
      }
    ]
  }
]
```

---

## 📊 Base de Datos

### Tablas Creadas

```sql
-- Tabla users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Tabla categories
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tabla products
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INTEGER DEFAULT 0,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla intermedia product_categories
CREATE TABLE product_categories (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

---

## ✅ Checklist de Buenas Prácticas Implementadas

- [x] **Separación de capas:** Controller → Service → Repository
- [x] **DTOs para entrada/salida:** No exponer entidades directamente
- [x] **Modelos de dominio:** Lógica de negocio en `Product` y `User`
- [x] **Validación de null:** Evitar NullPointerException
- [x] **Inicialización de colecciones:** Prevenir NPE en Sets
- [x] **Manejo de excepciones:** `NotFoundException` personalizada
- [x] **FetchType apropiado:** EAGER para owner, LAZY para collections grandes
- [x] **Métodos helper:** `addCategorie()` para encapsular lógica
- [x] **Factory methods:** `fromDto()`, `fromEntity()`, `toResponseDto()`
- [x] **Repository queries personalizados:** `findByOwnerId()`

---

## 🎓 Conceptos Clave Aprendidos

### 1. **Clean Architecture**
Separar responsabilidades en capas independientes para facilitar mantenimiento y testing.

### 2. **Domain-Driven Design (DDD)**
El modelo de dominio (`Product`, `User`) contiene la lógica de negocio, no las entidades JPA.

### 3. **JPA Entity Lifecycle**
- `new` → Transient (no gestionado por Hibernate)
- `persist` → Managed (en contexto de persistencia)
- `merge` → Managed (reattach de entidad detached)
- `remove` → Removed (marcado para eliminar)

### 4. **N+1 Query Problem**
```java
// ❌ LAZY genera N+1 queries
users.forEach(user -> {
    user.getProducts().forEach(product -> {
        // SELECT para cada user
    });
});

// ✅ EAGER o JOIN FETCH evita esto
```

### 5. **Transacciones**
`@Transactional` asegura que las operaciones sean atómicas y el EntityManager esté activo.

---

## 🔧 Configuración del Proyecto

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/devdb
    username: postgres
    password: tu_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

### build.gradle.kts
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("org.postgresql:postgresql")
}
```

---

## 📚 Recursos Adicionales

- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate Fetching Strategies](https://vladmihalcea.com/eager-fetching-is-a-code-smell/)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 👨‍💻 Autor
Desarrollo realizado paso a paso para implementar sistema de productos con Spring Boot y JPA.

**Fecha:** Enero 2026  
**Stack:** Spring Boot 4.0.0, PostgreSQL 16, Hibernate 7.1.8
