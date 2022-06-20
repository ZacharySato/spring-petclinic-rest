Для запуска нам понадобится:
Образ с DB Postgress

Как запустить сервис:
1: Создать .jar файл с помощью maven: mvn package
2: Создать образ проекта командой docker buildx build --tag online_homework .
3: Запустить compose, командой: docker-compose -f docker-compose-postgres.yml up -d 

