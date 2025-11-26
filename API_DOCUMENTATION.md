# API Documentation - Logistics Management System

## Базовая информация

- **Base URL**: `http://localhost:8080/api`
- **Аутентификация**: JWT Token (Bearer Token)
- **Content-Type**: `application/json`
- **Формат дат**: ISO 8601 (например: `2024-01-15T10:30:00`)

### Аутентификация

Большинство эндпоинтов требуют JWT токен в заголовке:
```
Authorization: Bearer <token>
```

Токен получается через эндпоинты `/auth/register` или `/auth/login`.

---

## Эндпоинты

### 1. Аутентификация (`/auth`)

#### 1.1 Регистрация
**POST** `/auth/register`

**Авторизация**: Не требуется

**Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string",
  "role": "string" // Возможные значения: "CLIENT", "COURIER", "DRIVER", "MANAGER", "ADMIN"
}
```

**Response** (200 OK):
```json
{
  "token": "string",
  "message": "Authentication successful"
}
```

---

#### 1.2 Вход
**POST** `/auth/login`

**Авторизация**: Не требуется

**Request Body**:
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response** (200 OK):
```json
{
  "token": "string",
  "message": "Authentication successful"
}
```

---

### 2. Города (`/cities`)

#### 2.1 Создать город
**POST** `/cities`

**Авторизация**: Требуется

**Request Body**:
```json
{
  "name": "string",
  "country": "string"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Москва",
  "country": "Россия"
}
```

---

#### 2.2 Получить город по ID
**GET** `/cities/{cityId}`

**Авторизация**: Требуется

**Path Parameters**:
- `cityId` (Long) - ID города

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Москва",
  "country": "Россия"
}
```

---

#### 2.3 Получить все города
**GET** `/cities`

**Авторизация**: Требуется

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "Москва",
    "country": "Россия"
  },
  {
    "id": 2,
    "name": "Санкт-Петербург",
    "country": "Россия"
  }
]
```

---

#### 2.4 Получить город по имени
**GET** `/cities/name/{name}`

**Авторизация**: Требуется

**Path Parameters**:
- `name` (String) - Название города

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Москва",
  "country": "Россия"
}
```

---

#### 2.5 Обновить город
**PUT** `/cities/{cityId}`

**Авторизация**: Требуется

**Path Parameters**:
- `cityId` (Long) - ID города

**Request Body**:
```json
{
  "name": "string",
  "country": "string"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Москва",
  "country": "Россия"
}
```

---

#### 2.6 Удалить город
**DELETE** `/cities/{cityId}`

**Авторизация**: Требуется (только ADMIN)

**Path Parameters**:
- `cityId` (Long) - ID города

**Response** (204 No Content)

---

#### 2.7 Проверить существование города
**GET** `/cities/exists/{name}`

**Авторизация**: Требуется

**Path Parameters**:
- `name` (String) - Название города

**Response** (200 OK):
```json
true
```

---

### 3. Заказы (`/orders`)

#### 3.1 Создать заказ
**POST** `/orders`

**Авторизация**: Требуется (CLIENT, ADMIN)

**Request Body**:
```json
{
  "driverId": 1,
  "fromPointId": 1,
  "toPointId": 2,
  "pickupDatePlanned": "2024-01-15T10:00:00",
  "deliveryDatePlanned": "2024-01-16T14:00:00",
  "deliveryCost": 500.00,
  "paymentMethod": "CASH",
  "notes": "string",
  "specialInstructions": "string"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "orderNumber": "ORD-2024-001",
  "driverId": 1,
  "fromPointId": 1,
  "toPointId": 2,
  "status": "CREATED",
  "pickupDatePlanned": "2024-01-15T10:00:00",
  "pickupDateActual": null,
  "deliveryDatePlanned": "2024-01-16T14:00:00",
  "deliveryDateActual": null,
  "arrivedAtPickupPoint": null,
  "deliveryCost": 500.00,
  "isPaid": false,
  "paymentMethod": "CASH",
  "notes": "string",
  "specialInstructions": "string",
  "currentLocation": null,
  "createdAt": "2024-01-14T12:00:00",
  "updatedAt": "2024-01-14T12:00:00"
}
```

---

#### 3.2 Обновить заказ
**PUT** `/orders/{orderId}`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Path Parameters**:
- `orderId` (Long) - ID заказа

**Request Body**:
```json
{
  "driverId": 1,
  "fromPointId": 1,
  "toPointId": 2,
  "status": "IN_TRANSIT",
  "pickupDatePlanned": "2024-01-15T10:00:00",
  "pickupDateActual": "2024-01-15T10:30:00",
  "deliveryDatePlanned": "2024-01-16T14:00:00",
  "deliveryDateActual": null,
  "arrivedAtPickupPoint": null,
  "deliveryCost": 500.00,
  "isPaid": true,
  "paymentMethod": "CASH",
  "notes": "string",
  "specialInstructions": "string",
  "currentLocation": "Москва, ул. Ленина, 10"
}
```

**Response** (200 OK): OrderDto (см. выше)

---

#### 3.3 Получить заказ по ID
**GET** `/orders/{orderId}`

**Авторизация**: Требуется

**Path Parameters**:
- `orderId` (Long) - ID заказа

**Response** (200 OK): OrderDto

---

#### 3.4 Получить заказ по номеру
**GET** `/orders/number/{orderNumber}`

**Авторизация**: Требуется

**Path Parameters**:
- `orderNumber` (String) - Номер заказа (например: "ORD-2024-001")

**Response** (200 OK): OrderDto

---

#### 3.5 Получить заказы водителя
**GET** `/orders/driver`

**Авторизация**: Требуется (DRIVER, ADMIN)

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-2024-001",
    ...
  }
]
```

---

#### 3.6 Получить заказы по статусу
**GET** `/orders/status/{status}`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Path Parameters**:
- `status` (OrderStatus) - Статус заказа

**Response** (200 OK): Array of OrderDto

---

#### 3.7 Получить заказы по диапазону дат
**GET** `/orders/date-range?startDate={startDate}&endDate={endDate}`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Query Parameters**:
- `startDate` (LocalDateTime) - Начальная дата (ISO 8601)
- `endDate` (LocalDateTime) - Конечная дата (ISO 8601)

**Пример**: `/orders/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59`

**Response** (200 OK): Array of OrderDto

---

#### 3.8 Обновить статус заказа
**PUT** `/orders/{orderId}/status?status={status}`

**Авторизация**: Требуется (ADMIN, MANAGER, DRIVER)

**Path Parameters**:
- `orderId` (Long) - ID заказа

**Query Parameters**:
- `status` (OrderStatus) - Новый статус

**Response** (200 OK): OrderDto

---

#### 3.9 Назначить водителя на заказ
**PUT** `/orders/{orderId}/assign-driver?driverId={driverId}`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Path Parameters**:
- `orderId` (Long) - ID заказа

**Query Parameters**:
- `driverId` (Long) - ID водителя

**Response** (200 OK): OrderDto

---

#### 3.10 Получить просроченные заказы
**GET** `/orders/overdue`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Response** (200 OK): Array of OrderDto

---

#### 3.11 Удалить заказ
**DELETE** `/orders/{orderId}`

**Авторизация**: Требуется (только ADMIN)

**Path Parameters**:
- `orderId` (Long) - ID заказа

**Response** (204 No Content)

---

### 4. Пункты выдачи (`/pickup-points`)

#### 4.1 Создать пункт выдачи
**POST** `/pickup-points`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Request Body**:
```json
{
  "name": "Пункт выдачи №1",
  "address": "Москва, ул. Ленина, 10",
  "cityId": 1,
  "phone": "+7 (999) 123-45-67",
  "workingHours": "09:00-18:00",
  "latitude": 55.7558,
  "longitude": 37.6173,
  "maxCapacity": 100,
  "maxPackageWeight": 30.0,
  "hasColdStorage": true,
  "type": "WAREHOUSE",
  "managerId": 1
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Пункт выдачи №1",
  "address": "Москва, ул. Ленина, 10",
  "cityId": 1,
  "phone": "+7 (999) 123-45-67",
  "workingHours": "09:00-18:00",
  "latitude": 55.7558,
  "longitude": 37.6173,
  "maxCapacity": 100,
  "currentLoad": 0,
  "maxPackageWeight": 30.0,
  "hasColdStorage": true,
  "isActive": true,
  "type": "WAREHOUSE",
  "managerId": 1,
  "createdAt": "2024-01-14T12:00:00",
  "updatedAt": "2024-01-14T12:00:00"
}
```

---

#### 4.2 Обновить пункт выдачи
**PUT** `/pickup-points/{pickupPointId}`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Path Parameters**:
- `pickupPointId` (Long) - ID пункта выдачи

**Request Body**:
```json
{
  "name": "Пункт выдачи №1",
  "address": "Москва, ул. Ленина, 10",
  "cityId": 1,
  "phone": "+7 (999) 123-45-67",
  "workingHours": "09:00-18:00",
  "latitude": 55.7558,
  "longitude": 37.6173,
  "maxCapacity": 100,
  "maxPackageWeight": 30.0,
  "hasColdStorage": true,
  "isActive": true,
  "type": "WAREHOUSE",
  "managerId": 1
}
```

**Response** (200 OK): PickupPointDto

---

#### 4.3 Получить пункт выдачи по ID
**GET** `/pickup-points/{pickupPointId}`

**Авторизация**: Требуется

**Path Parameters**:
- `pickupPointId` (Long) - ID пункта выдачи

**Response** (200 OK): PickupPointDto

---

#### 4.4 Получить все пункты выдачи
**GET** `/pickup-points`

**Авторизация**: Требуется

**Response** (200 OK): Array of PickupPointDto

---

#### 4.5 Получить пункты выдачи по городу
**GET** `/pickup-points/city/{cityId}`

**Авторизация**: Требуется

**Path Parameters**:
- `cityId` (Long) - ID города

**Response** (200 OK): Array of PickupPointDto

---

#### 4.6 Получить пункты выдачи по типу
**GET** `/pickup-points/type/{type}`

**Авторизация**: Требуется

**Path Parameters**:
- `type` (PickupPointType) - Тип пункта выдачи

**Response** (200 OK): Array of PickupPointDto

---

#### 4.7 Получить доступные пункты выдачи в городе
**GET** `/pickup-points/available/city/{cityId}`

**Авторизация**: Требуется

**Path Parameters**:
- `cityId` (Long) - ID города

**Response** (200 OK): Array of PickupPointDto

---

#### 4.8 Получить доступные пункты выдачи с холодильником в городе
**GET** `/pickup-points/available/cold-storage/city/{cityId}`

**Авторизация**: Требуется

**Path Parameters**:
- `cityId` (Long) - ID города

**Response** (200 OK): Array of PickupPointDto

---

#### 4.9 Обновить загрузку пункта выдачи
**PUT** `/pickup-points/{pickupPointId}/load?loadChange={loadChange}`

**Авторизация**: Требуется (ADMIN, MANAGER)

**Path Parameters**:
- `pickupPointId` (Long) - ID пункта выдачи

**Query Parameters**:
- `loadChange` (Integer) - Изменение загрузки (может быть отрицательным для уменьшения)

**Response** (200 OK): PickupPointDto

---

#### 4.10 Удалить пункт выдачи
**DELETE** `/pickup-points/{pickupPointId}`

**Авторизация**: Требуется (только ADMIN)

**Path Parameters**:
- `pickupPointId` (Long) - ID пункта выдачи

**Response** (204 No Content)

---

### 5. Товары (`/products`)

#### 5.1 Создать товар
**POST** `/products`

**Авторизация**: Требуется (CLIENT, ADMIN)

**Request Body**:
```json
{
  "name": "Ноутбук",
  "description": "Игровой ноутбук",
  "weight": 2.5,
  "length": 35.0,
  "width": 25.0,
  "height": 5.0,
  "category": "ELECTRONICS",
  "isFragile": true,
  "requiresColdStorage": false,
  "isValuable": true,
  "receiverId": 2
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "Ноутбук",
  "description": "Игровой ноутбук",
  "weight": 2.5,
  "length": 35.0,
  "width": 25.0,
  "height": 5.0,
  "category": "ELECTRONICS",
  "statusHistoryIds": [],
  "isFragile": true,
  "requiresColdStorage": false,
  "isValuable": true,
  "declaredValue": null,
  "senderId": 1,
  "receiverId": 2,
  "orderId": null,
  "createdAt": "2024-01-14T12:00:00",
  "updatedAt": "2024-01-14T12:00:00"
}
```

---

#### 5.2 Обновить товар
**PUT** `/products/{productId}`

**Авторизация**: Требуется (CLIENT, ADMIN)

**Path Parameters**:
- `productId` (Long) - ID товара

**Request Body**:
```json
{
  "name": "Ноутбук",
  "description": "Игровой ноутбук",
  "weight": 2.5,
  "length": 35.0,
  "width": 25.0,
  "height": 5.0,
  "category": "ELECTRONICS",
  "isFragile": true,
  "requiresColdStorage": false,
  "isValuable": true,
  "declaredValue": 50000.00,
  "receiverId": 2,
  "orderId": 1
}
```

**Response** (200 OK): ProductDto

---

#### 5.3 Получить товар по ID
**GET** `/products/{productId}`

**Авторизация**: Требуется (CLIENT, ADMIN, MANAGER, DRIVER)

**Path Parameters**:
- `productId` (Long) - ID товара

**Response** (200 OK): ProductDto

---

#### 5.4 Обновить статус товара
**PUT** `/products/{productId}/status?status={status}`

**Авторизация**: Требуется (ADMIN, MANAGER, DRIVER)

**Path Parameters**:
- `productId` (Long) - ID товара

**Query Parameters**:
- `status` (ProductStatus) - Новый статус

**Response** (200 OK): ProductDto

---

#### 5.5 Удалить товар
**DELETE** `/products/{productId}`

**Авторизация**: Требуется (CLIENT, ADMIN)

**Path Parameters**:
- `productId` (Long) - ID товара

**Response** (204 No Content)

---

#### 5.6 Получить мои товары
**GET** `/products/my-products`

**Авторизация**: Требуется (CLIENT, ADMIN)

**Response** (200 OK): Array of ProductDto

**Примечание**: В текущей реализации возвращает пустой массив.

---

### 6. Тестовые эндпоинты (`/api`)

#### 6.1 Публичный эндпоинт
**GET** `/api/public`

**Авторизация**: Не требуется

**Response** (200 OK):
```json
"Это публичный эндпоинт, доступный всем"
```

---

#### 6.2 Профиль пользователя
**GET** `/api/user`

**Авторизация**: Требуется

**Response** (200 OK):
```json
{
  "sub": "username",
  "roles": ["CLIENT"],
  ...
}
```

---

#### 6.3 Административный эндпоинт
**GET** `/api/admin`

**Авторизация**: Требуется (только ADMIN)

**Response** (200 OK):
```json
"Это эндпоинт только для администраторов"
```

---

## Перечисления (Enums)

### OrderStatus (Статус заказа)
- `CREATED` - Создан
- `ASSIGNED_TO_DRIVER` - Назначен водителю
- `PICKUP_IN_PROGRESS` - Забор в процессе
- `PICKED_UP` - Забран
- `IN_TRANSIT` - В пути
- `AT_PICKUP_POINT` - В пункте выдачи
- `OUT_FOR_DELIVERY` - На доставке
- `DELIVERED` - Доставлен
- `RETURNED_TO_SENDER` - Возвращен отправителю
- `CANCELLED` - Отменен
- `LOST` - Утерян
- `DAMAGED` - Поврежден

### PickupPointType (Тип пункта выдачи)
- `WAREHOUSE` - Склад
- `RETAIL_STORE` - Розничный магазин
- `POST_OFFICE` - Почтовое отделение
- `COURIER_OFFICE` - Офис курьерской службы
- `MOBILE_POINT` - Мобильный пункт
- `HOME_DELIVERY` - Адресная доставка

### ProductStatus (Статус товара)
- `CREATED` - Заказ создан
- `READY_FOR_PICKUP` - Готов к забору
- `IN_TRANSIT` - В пути
- `AT_PICKUP_POINT` - В пункте выдачи
- `DELIVERED` - Доставлен
- `RETURNED` - Возвращен
- `LOST` - Утерян
- `DAMAGED` - Поврежден

### ProductCategory (Категория товара)
- `DOCUMENTS` - Документы
- `CLOTHING` - Одежда
- `ELECTRONICS` - Электроника
- `FOOD` - Продукты питания
- `MEDICINE` - Медикаменты
- `FRAGILE` - Хрупкие товары
- `VALUABLES` - Ценные вещи
- `FURNITURE` - Мебель
- `APPLIANCES` - Бытовая техника
- `BOOKS` - Книги
- `OTHER` - Прочее

### UserRole (Роль пользователя)
- `CLIENT` - Клиент
- `COURIER` - Курьер
- `DRIVER` - Водитель
- `MANAGER` - Менеджер пункта выдачи
- `ADMIN` - Администратор

### PaymentMethod (Способ оплаты)
- `CASH` - Наличные
- `CARD` - Банковская карта
- `BANK_TRANSFER` - Банковский перевод
- `ELECTRONIC_WALLET` - Электронный кошелек
- `CRYPTO` - Криптовалюта

---

## Коды ответов HTTP

- `200 OK` - Успешный запрос
- `204 No Content` - Успешное удаление (без тела ответа)
- `400 Bad Request` - Неверный запрос
- `401 Unauthorized` - Требуется аутентификация
- `403 Forbidden` - Недостаточно прав доступа
- `404 Not Found` - Ресурс не найден
- `500 Internal Server Error` - Внутренняя ошибка сервера

---

## Обработка ошибок

При возникновении ошибки сервер возвращает JSON с описанием:

```json
{
  "timestamp": "2024-01-14T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "City not found with id: 1",
  "path": "/cities/1"
}
```

### Возможные исключения:
- `CityNotFoundException` - Город не найден
- `OrderNotFoundException` - Заказ не найден
- `PickupPointNotFoundException` - Пункт выдачи не найден
- `ProductNotFoundException` - Товар не найден
- `UserAlreadyExistsException` - Пользователь уже существует
- `InsufficientCapacityException` - Недостаточно места в пункте выдачи
- `InvalidStatusTransitionException` - Недопустимый переход статуса

---

## Примеры использования

### Пример 1: Регистрация и создание заказа

```javascript
// 1. Регистрация
const registerResponse = await fetch('http://localhost:8080/api/auth/register', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    username: 'john_doe',
    email: 'john@example.com',
    password: 'password123',
    firstName: 'John',
    lastName: 'Doe',
    role: 'CLIENT'
  })
});

const { token } = await registerResponse.json();

// 2. Создание заказа
const orderResponse = await fetch('http://localhost:8080/api/orders', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({
    driverId: 1,
    fromPointId: 1,
    toPointId: 2,
    pickupDatePlanned: '2024-01-15T10:00:00',
    deliveryDatePlanned: '2024-01-16T14:00:00',
    deliveryCost: 500.00,
    paymentMethod: 'CASH',
    notes: 'Хрупкий груз',
    specialInstructions: 'Осторожно!'
  })
});

const order = await orderResponse.json();
```

### Пример 2: Получение доступных пунктов выдачи

```javascript
const response = await fetch('http://localhost:8080/api/pickup-points/available/city/1', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const pickupPoints = await response.json();
```

---

## Примечания

1. Все даты и время передаются в формате ISO 8601 (UTC)
2. Десятичные числа (BigDecimal) передаются как числа в JSON
3. JWT токен действителен 24 часа (86400000 миллисекунд)
4. CORS настроен для всех источников (`*`)
5. Swagger UI доступен по адресу: `http://localhost:8080/api/swagger-ui.html`
6. API документация (OpenAPI): `http://localhost:8080/api/v3/api-docs`

---

## Структуры данных

### CityDto
```typescript
interface CityDto {
  id: number;
  name: string;
  country: string;
}
```

### OrderDto
```typescript
interface OrderDto {
  id: number;
  orderNumber: string;
  driverId: number | null;
  fromPointId: number;
  toPointId: number;
  status: OrderStatus;
  pickupDatePlanned: string; // ISO 8601
  pickupDateActual: string | null;
  deliveryDatePlanned: string; // ISO 8601
  deliveryDateActual: string | null;
  arrivedAtPickupPoint: string | null;
  deliveryCost: number;
  isPaid: boolean;
  paymentMethod: PaymentMethod;
  notes: string | null;
  specialInstructions: string | null;
  currentLocation: string | null;
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
}
```

### PickupPointDto
```typescript
interface PickupPointDto {
  id: number;
  name: string;
  address: string;
  cityId: number;
  phone: string;
  workingHours: string;
  latitude: number;
  longitude: number;
  maxCapacity: number;
  currentLoad: number;
  maxPackageWeight: number;
  hasColdStorage: boolean;
  isActive: boolean;
  type: PickupPointType;
  managerId: number | null;
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
}
```

### ProductDto
```typescript
interface ProductDto {
  id: number;
  name: string;
  description: string | null;
  weight: number;
  length: number;
  width: number;
  height: number;
  category: ProductCategory;
  statusHistoryIds: number[];
  isFragile: boolean;
  requiresColdStorage: boolean;
  isValuable: boolean;
  declaredValue: number | null;
  senderId: number;
  receiverId: number;
  orderId: number | null;
  createdAt: string; // ISO 8601
  updatedAt: string; // ISO 8601
}
```

### AuthResponse
```typescript
interface AuthResponse {
  token: string;
  message: string;
}
```

---

**Версия документации**: 1.0  
**Дата обновления**: 2024-01-14

