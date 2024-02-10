[![Bot Build](https://github.com/IlyaKuzmichev/Tinkoff-java-course-2024-backend/actions/workflows/bot.yml/badge.svg)](https://github.com/IlyaKuzmichev/Tinkoff-java-course-2024-backend/actions/workflows/bot.yml)
[![Scrapper Build](https://github.com/IlyaKuzmichev/Tinkoff-java-course-2024-backend/actions/workflows/scrapper.yml/badge.svg)](https://github.com/IlyaKuzmichev/Tinkoff-java-course-2024-backend/actions/workflows/scrapper.yml)

# Link Tracker

ФИО: Кузьмичев Илья Александрович

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 21` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.
