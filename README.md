
---

# 🛒 E-Commerce Backend

Это серверная часть интернет-магазина, разработанная с использованием Java, Spring Boot и JPA. Проект предоставляет REST API для управления товарами, категориями и заказами. ([Online Shopping platform(E-commerce website) - GitHub](https://github.com/hariomthadke/E-Commerce-Website?utm_source=chatgpt.com))

## 🚀 Технологии

- Java 17
- Spring Boot
- Spring Data JPA
- MapStruct
- Lombok
- PostgreSQL
- Spring Security
- Spring Cloud Gateway (если используется) ([react-ecommerce · GitHub Topics](https://github.com/topics/react-ecommerce?utm_source=chatgpt.com), [Online Shopping platform(E-commerce website) - GitHub](https://github.com/hariomthadke/E-Commerce-Website?utm_source=chatgpt.com), [ecommerce-website · GitHub Topics](https://github.com/topics/ecommerce-website?utm_source=chatgpt.com), [e-commerce/README.md at main - GitHub](https://github.com/GonzaloVolonterio/e-commerce/blob/main/README.md?utm_source=chatgpt.com), [README.md - Jaweki/modern-ecommerce-product-page - GitHub](https://github.com/Jaweki/modern-ecommerce-product-page/blob/main/README.md?utm_source=chatgpt.com))

## 📦 Структура проекта

```
e-commerce/
├── src/
│   ├── main/
│   │   ├── java/yers/dev/products/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   └── resources/
│       └── application.yml
├── pom.xml
└── README.md
```

## 📚 Основные сущности

- `Products`: Сущность товара с полями `name`, `description`, `price`, `stock_quantity`, `category`.
- `Category`: Перечисление категорий товаров (например, ELECTRONICS, CLOTHING).
- `ProductsDto`: DTO для передачи данных о товарах.
- `ProductsMapper`: Интерфейс для маппинга между `Products` и `ProductsDto`.

## 🔧 Настройка и запуск

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/Yersdev/e-commerce.git
   cd e-commerce
   ```


2. Настройте базу данных PostgreSQL и укажите параметры подключения в `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/your_db
       username: your_username
       password: your_password
   ```
([e-commerce/README.md at main - GitHub](https://github.com/GonzaloVolonterio/e-commerce/blob/main/README.md?utm_source=chatgpt.com))

3. Соберите и запустите приложение:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```


## 📬 API Эндпоинты

| Метод | Эндпоинт           | Описание                         |
|-------|--------------------|----------------------------------|
| GET   | `/api/products`    | Получить список всех товаров     |
| GET   | `/api/products/{id}` | Получить товар по ID             |
| POST  | `/api/products`    | Создать новый товар              |
| PUT   | `/api/products/{id}` | Обновить существующий товар      |
| DELETE| `/api/products/{id}` | Удалить товар по ID              |
| GET   | `/api/products/category/{category}` | Получить товары по категории | ([e-commerce-project/README.md at main - GitHub](https://github.com/Josehower/e-commerce-project/blob/main/README.md?utm_source=chatgpt.com))

## 🛠️ Примеры запросов

**Получить все товары:**
```bash
curl -X GET http://localhost:8080/api/products
```


**Создать новый товар:**
```bash
curl -X POST http://localhost:8080/api/products \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Smartphone",
           "description": "Latest model",
           "price": 699.99,
           "stock_quantity": 50,
           "category": "ELECTRONICS"
         }'
```


## 📝 Примечания

- Убедитесь, что база данных PostgreSQL запущена и доступна.
- Для работы с MapStruct необходимо настроить соответствующие зависимости и аннотации.
- Если используется Spring Cloud Gateway, настройте маршруты в соответствии с требованиями.

---

Если потребуется более подробная информация или помощь с другими разделами `README.md`, пожалуйста, сообщите! 