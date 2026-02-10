# PKB — Personal Knowledge Base

PKB — это веб-приложение для хранения и организации личных знаний:
заметок, ссылок и идей.

![Демонстрация основной страницы](src/main/resources/static/images/account-demo.png)

## Основные возможности
- Рабочий API (базово) [✓]
- Регистрация и аутентификация пользователей [✓]
- Рабочий фронт [✓]
- Создание и редактирование заметок [✓]
- Rest Assured тесты [✓]
- Selenium UI тесты [✓]
- Unit тесты JUnit5 [✓]
- Allure reports [✓]
- JaCoCo integrations [✓]
- CI/CD pipeline [?]
- Категории и теги [?]
- Поиск по заметкам [?]
- Архивация и избранное [?]

## Технологии разработки
- Java 17
- Spring Boot 3
- Spring Security
- PostgreSQL
- HTML + CSS

## Технологии тестирования
- JUnit 5
- Selenium
- Rest Assured
- Mockito
- Allure Reports
- JaCoCo

# Запуск проекта

### 1. Клонировать репозиторий

`git clone https://github.com/depedence/personal-knowledge-base`

### 2. Поднять docker контейнеры с бд и веб-приложением

`docker-compose up --build`

### 3. После запуска контейнеров

После успешного запуска перейти на `localhost:8080/` и выполнить регистрацию, а после логин в созданный аккаунт
