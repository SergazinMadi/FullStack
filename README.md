# FullStack Security Configuration

Этот проект был обновлён с улучшенной системой безопасности JWT на основе архитектуры проекта Quryltai.

## Основные улучшения

### 1. JWT Фильтр (JwtAuthFilter)
- Улучшенная обработка токенов с использованием `StringUtils` для безопасной проверки
- Лучшая обработка Security Context
- Константы для Bearer префикса и заголовков

### 2. JWT Сервис (JwtService)
- Метод `extractTokenData()` для извлечения данных пользователя из токена
- Улучшенная генерация токенов с включением дополнительных claims (id, role, email)
- Лучшая обработка ошибок и валидация входных параметров
- Метод `extractJwtToken()` для извлечения токена из HTTP запроса

### 3. Конфигурация безопасности (SecurityConfig)
- Улучшенная CORS конфигурация
- Добавлен PasswordEncoder (BCrypt)
- Правильная настройка AuthenticationProvider
- Более гибкие правила доступа к эндпоинтам

### 4. Сервис пользователей (UserService)
- Исправлена архитектура с правильным разделением интерфейса и реализации
- Упрощённая логика загрузки пользователей
- Лучшая интеграция с Spring Security

### 5. Контроллер аутентификации (AuthController)
- Полноценные методы регистрации и входа
- Правильная обработка ошибок
- Хеширование паролей
- Проверка существования пользователей

## API Эндпоинты

### Аутентификация
- `POST /auth/register` - Регистрация нового пользователя
- `POST /auth/login` - Вход в систему

### Тестовые эндпоинты
- `GET /api/public` - Публичный эндпоинт (без аутентификации)
- `GET /api/user` - Получение данных пользователя (требует токен)
- `GET /api/admin` - Админский эндпоинт (требует роль ADMIN)

## Использование

### Регистрация
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}'
```

### Вход
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}'
```

### Использование токена
```bash
curl -X GET http://localhost:8080/api/user \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Роли пользователей
- `CLIENT` - Клиент (роль по умолчанию при регистрации)
- `DRIVER` - Водитель
- `MANAGER` - Менеджер пункта выдачи
- `ADMIN` - Администратор

## Зависимости
Добавлены следующие зависимости:
- `commons-lang3` - для StringUtils и других утилит
- `spring-boot-starter-security` - Spring Security
- `jjwt` - библиотека для работы с JWT

## Конфигурация
В `application.yml` настроены:
- `jwt.secret` - секретный ключ для подписи токенов
- `jwt.expiration` - время жизни токена (по умолчанию 24 часа)
- Настройки базы данных PostgreSQL
