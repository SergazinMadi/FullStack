# Full Stack Logistics Management System

Система управления логистикой для доставки грузов с поддержкой различных ролей пользователей.

## Функциональность

### Роли пользователей
- **CLIENT** - Клиент (создание заказов, отслеживание грузов)
- **COURIER** - Курьер (доставка грузов)
- **DRIVER** - Водитель (транспортировка грузов)
- **MANAGER** - Менеджер пункта выдачи (управление пунктами)
- **ADMIN** - Администратор (полный доступ)

### Основные модули

#### 1. Аутентификация и авторизация
- Регистрация пользователей
- Вход в систему с JWT токенами
- Ролевая модель доступа

#### 2. Управление продуктами
- Создание и редактирование грузов
- Отслеживание статусов
- История изменений статусов

#### 3. Управление заказами
- Создание заказов доставки
- Назначение водителей
- Отслеживание статусов заказов
- Планирование маршрутов

#### 4. Управление городами
- Добавление городов
- Поиск по названию

#### 5. Управление пунктами выдачи
- Создание и настройка пунктов
- Управление загруженностью
- Поддержка холодильного хранения

## Технологии

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Tokens)
- **Mapping**: MapStruct
- **Documentation**: Swagger/OpenAPI
- **Build Tool**: Maven
- **Java Version**: 17

## Установка и запуск

### Требования
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Настройка базы данных
1. Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE logistics_db;
```

2. Настройте подключение в `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/logistics_db
    username: your_username
    password: your_password
```

### Запуск приложения
```bash
# Клонирование репозитория
git clone <repository-url>
cd FullStack

# Сборка проекта
mvn clean install

# Запуск приложения
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8080/api`

## API Документация

После запуска приложения Swagger UI доступен по адресу:
`http://localhost:8080/api/swagger-ui.html`

## Основные эндпоинты

### Аутентификация
- `POST /auth/register` - Регистрация пользователя
- `POST /auth/login` - Вход в систему

### Продукты
- `POST /products` - Создание продукта
- `GET /products/{id}` - Получение продукта
- `PUT /products/{id}` - Обновление продукта
- `DELETE /products/{id}` - Удаление продукта
- `PUT /products/{id}/status` - Изменение статуса

### Заказы
- `POST /orders` - Создание заказа
- `GET /orders/{id}` - Получение заказа
- `PUT /orders/{id}` - Обновление заказа
- `PUT /orders/{id}/status` - Изменение статуса заказа
- `PUT /orders/{id}/assign-driver` - Назначение водителя

### Города
- `GET /cities` - Список городов
- `POST /cities` - Создание города (только ADMIN)
- `GET /cities/{id}` - Получение города

### Пункты выдачи
- `GET /pickup-points` - Список пунктов выдачи
- `GET /pickup-points/city/{cityId}` - Пункты выдачи по городу
- `POST /pickup-points` - Создание пункта выдачи
- `GET /pickup-points/available/city/{cityId}` - Доступные пункты

## Конфигурация

### Переменные окружения
- `DB_USERNAME` - Имя пользователя БД (по умолчанию: postgres)
- `DB_PASSWORD` - Пароль БД (по умолчанию: password)
- `JWT_SECRET` - Секретный ключ для JWT (по умолчанию: mySecretKey...)
- `JWT_EXPIRATION` - Время жизни токена в мс (по умолчанию: 86400000)

### Логирование
Логи сохраняются в файл `logs/application.log` и выводятся в консоль.

## Структура проекта

```
src/main/java/org/example/fullstack/
├── config/                 # Конфигурация Spring
├── controller/             # REST контроллеры
├── db/
│   ├── dto/               # Data Transfer Objects
│   ├── enums/             # Перечисления
│   ├── mapper/            # MapStruct мапперы
│   ├── model/             # JPA сущности
│   └── repository/        # JPA репозитории
├── exception/             # Обработка исключений
└── service/               # Бизнес-логика
    ├── impl/              # Реализации сервисов
    └── util/              # Утилиты
```

## Разработка

### Добавление новых функций
1. Создайте модель в `db/model/`
2. Создайте репозиторий в `db/repository/`
3. Создайте DTO в `db/dto/`
4. Создайте маппер в `db/mapper/`
5. Создайте сервис в `service/`
6. Создайте контроллер в `controller/`

### Тестирование
```bash
mvn test
```

## Лицензия

Этот проект создан в образовательных целях.