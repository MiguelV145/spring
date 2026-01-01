# Pruebas de Validación - Módulo Products

## Casos de Prueba

### 1. ✅ Crear producto válido
**POST** `http://localhost:8080/api/products`

```json
{
  "name": "Laptop HP",
  "description": "Laptop de 15 pulgadas",
  "price": 899.99,
  "stock": 10
}
```

**Respuesta esperada:** 201 + ProductResponseDto con id, name, description, price, stock, createdAt

---

### 2. ❌ Error de validación - Nombre vacío
**POST** `http://localhost:8080/api/products`

```json
{
  "name": "",
  "description": "Laptop de 15 pulgadas",
  "price": 899.99,
  "stock": 10
}
```

**Respuesta esperada:** 400 Bad Request
```json
{
  "status": 400,
  "error": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "El nombre es obligatorio"
    }
  ],
  "timestamp": "2026-01-01T10:30:00"
}
```

---

### 3. ❌ Error de validación - Precio negativo
**POST** `http://localhost:8080/api/products`

```json
{
  "name": "Producto",
  "description": "Descripción",
  "price": -50,
  "stock": 5
}
```

**Respuesta esperada:** 400 Bad Request
```json
{
  "status": 400,
  "error": "Validation failed",
  "errors": [
    {
      "field": "price",
      "message": "El precio no puede ser negativo"
    }
  ],
  "timestamp": "2026-01-01T10:30:00"
}
```

---

### 4. ❌ Error de validación - Stock negativo
**POST** `http://localhost:8080/api/products`

```json
{
  "name": "Producto",
  "description": "Descripción",
  "price": 100,
  "stock": -5
}
```

**Respuesta esperada:** 400 Bad Request
```json
{
  "status": 400,
  "error": "Validation failed",
  "errors": [
    {
      "field": "stock",
      "message": "El stock no puede ser negativo"
    }
  ],
  "timestamp": "2026-01-01T10:30:00"
}
```

---

### 5. ❌ Conflicto lógico - Nombre duplicado
**POST** `http://localhost:8080/api/products` (segunda vez con mismo nombre)

```json
{
  "name": "Laptop HP",
  "description": "Descripción diferente",
  "price": 999.99,
  "stock": 5
}
```

**Respuesta esperada:** 409 Conflict
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un producto con el nombre: Laptop HP",
  "timestamp": "2026-01-01T10:30:00"
}
```

---

### 6. ❌ Recurso no encontrado - Producto inexistente
**GET** `http://localhost:8080/api/products/9999`

**Respuesta esperada:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado",
  "timestamp": "2026-01-01T10:30:00"
}
```

---

### 7. ❌ Recurso no encontrado - Actualizar producto inexistente
**PUT** `http://localhost:8080/api/products/9999`

```json
{
  "name": "Nuevo nombre",
  "description": "Nueva descripción",
  "price": 150,
  "stock": 20
}
```

**Respuesta esperada:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado",
  "timestamp": "2026-01-01T10:30:00"
}
```

---

### 8. ✅ Actualización parcial - válida
**PATCH** `http://localhost:8080/api/products/{id}`

```json
{
  "price": 799.99
}
```

**Respuesta esperada:** 200 OK + ProductResponseDto actualizado

---

### 9. ✅ Listar todos los productos
**GET** `http://localhost:8080/api/products`

**Respuesta esperada:** 200 OK + Array de ProductResponseDto

---

### 10. ✅ Eliminar producto existente
**DELETE** `http://localhost:8080/api/products/{id}`

**Respuesta esperada:** 200 OK + body vacío

---

### 11. ❌ Eliminar producto inexistente
**DELETE** `http://localhost:8080/api/products/9999`

**Respuesta esperada:** 404 Not Found
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado",
  "timestamp": "2026-01-01T10:30:00"
}
```

---

## Resumen de Formatos de Error

Todos los errores siguen el mismo patrón:

1. **Validación (400):**
   - `status`: 400
   - `error`: "Validation failed"
   - `errors[]`: Array con field + message
   - `timestamp`: LocalDateTime

2. **No encontrado (404):**
   - `status`: 404
   - `error`: "Not Found"
   - `message`: Descripción del error
   - `timestamp`: LocalDateTime

3. **Conflicto (409):**
   - `status`: 409
   - `error`: "Conflict"
   - `message`: Descripción del conflicto
   - `timestamp`: LocalDateTime
