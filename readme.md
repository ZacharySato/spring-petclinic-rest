Для запуска понадобится образ с Postgress

Как запустить сервис:
1. Создать .jar файл с помощью maven: mvn package 
2. Создать образ проекта командой: docker buildx build --tag online_homework . 
3. Запустить сборку командой: docker-compose -f docker-compose-postgres.yml up -d 

