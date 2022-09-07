# 🛠 QA Automation Интенсив: пишем автотесты на реальное web-приложение 

Что ж, на этом воркшопе вы сможете с нуля написать API и UI тесты на веб-приложение (настоящее! не тестовое), внедрить тесты в CI/CD и даже развернуть настоящий review app (слепок приложения с вашими изменениями). Не забудьте добавить финальный проект в ваше порфтолио, погнали!

## 1️⃣ Локальный запуск приложения

1. Создаем папку, где будем запускать проект
```bash
cd <your folder> #переходите в директорию с вашими проектами
mkdir workshop #cоздаете папку вокршопа
```
2. Запускаем TeamCity Server. Документация [тут](https://hub.docker.com/r/jetbrains/teamcity-server).

```bash
docker run --name teamcity-server-instance  \
    -v $(pwd)/datadir:/data/teamcity_server/datadir \
    -v $(pwd)/logs:/opt/teamcity/logs  \
    -p 8111:8111 \
    jetbrains/teamcity-server:
```

3. Сетап TeamCity Server
- открываем http://localhost:8111 в браузере
- кликаем Proceed дважды
- принимаем лицезию

## 2️⃣ Мануальный UI тест кейс

### Тест кейс

Прогоняем мануально базовый позитивный тест кейс: 
создание билд конфигурации CI/CD с выводом Hello, world! в консоль. 

Итак, проходим следующие шаги:
1. Кликаем на Create project
2. Вводим реальный Repository URL, кликаем на Proceed
3. Выбираем тип Step = Command line
4. Вводим в Custom Script степа echo "Hello, world!"
5. Нажимаем Run, чтобы запустить билд. 


### TeamCity Agent

Обнаруживаем, что для запуска нам не хватает хотя бы одного агента. 

1. Стартуем TeamCity Agent в соотвествии с описанием в [docker.hub](https://hub.docker.com/r/jetbrains/teamcity-agent).

```bash
docker run -e SERVER_URL="<url to TeamCity server>"  \ 
    -v $(pwd)/conf:/data/teamcity_agent/conf  \      
    jetbrains/teamcity-agent
```

где `<url to TeamCity server>` - это `http://IP:8111`.

IP можно узнать с помощью запроса в командной строке:
`ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1'`

### Проверка результата
Дожидаемся прохождения рана и убеждаемся, что во вкладке Build Log мы видим `Hello, world!`

2. Авторизуем агента в UI интерфейсе TeamCity

## 3️⃣ Мануальный API тест кейс

Я использую IntelliJ IDEA Ultimate 2022.1.4 версия. 
Можно скачать [тут](https://www.jetbrains.com/ru-ru/idea/download/) и воспользоваться trial версией. 

1. Создаем новый проект с Maven и Java.

###  Тест кейс as a code
2. Для создания текстового тест кейса скачать плагин [Test Management](https://plugins.jetbrains.com/plugin/15109-test-management) или просто текстовый файл
3. Создаем папку `test-cases` и в ней  New -> Test Suite.
4. Описание мануального тест кейса лежит [тут](https://github.com/Test-Automation-Projects/teamcity-tests-workshop/blob/basic-test/test-cases/Smoke_Test.t.md).

### Мануальный API тест as a code
5. Создаем папку `request-examples` и в ней новый [HTTP Request](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html) файл
6. Добавляем запросы по тест кейсу в соотвествии с [TeamCity API документацией](https://www.jetbrains.com/help/teamcity/teamcity-rest-api.html)
7. Описание мануального API тест кейса лежит [тут](https://github.com/Test-Automation-Projects/teamcity-tests-workshop/blob/basic-test/request-examples/Build_Configuration_Test.http).


## 4️⃣ Базовый API автотест
1. Создаем базовый автотест с исспользованием [TestNG](https://testng.org/doc/) и [RestAssured](https://rest-assured.io/).
2. Реализуем [спецификацию](https://www.javadoc.io/doc/io.rest-assured/rest-assured/3.1.1/io/restassured/specification/RequestSpecification.html) в RestAssured.
3. Добавляем сериализацию из JSON в объект. Для создания data классов используем [Lombok](https://projectlombok.org/) и его аннотации `@Data` и `@Builder`.
4. Реализуем ассерт корректного значения статус кода и поля JSON.


Результат урока в ветке [basic-test](https://github.com/Test-Automation-Projects/teamcity-tests-workshop/tree/basic-test), сам тест [BuildConfigurationTest](https://github.com/Test-Automation-Projects/teamcity-tests-workshop/blob/basic-test/src/test/java/api/BuildConfigurationTest.java).

## 5️⃣ Создание репозитория в GitHub
Теперь нам необходимо сохранить наши изменения в GitHub.

1. Создаем репозиторий
2. Копируем Repository URL
3. Открываем IDEA и в меню `VCS` -> `Create repository`
4. Добавляем remote репозиорий: в меню `VCS` -> `Manage Remotes` -> `+` -> вставляем скопированное значение Repository URL из GitHub
5. Создаем новую ветку и переходим на неё: в терминале `git checkout -b basic-test`
6. Добавляем все файлы, которые мы хотим закоммитить (сохранить в версии) в Git с помощью: клик правой кнопкой мыши по файлу -> `Git` -> `Add`
7. Коммитим изменения, переходя в вкладку `Commit` и выбираем файлы, которые хотим закоммитить, пишем  Commit message, далее жмём `Commit` -> `Anyway commit`. 
8. Теперь остается отправить коммит на удаленный репозиторий: в меню `VSC` -> `Push` -> проверяем изменения -> `Push`
9. Проверяем, что изменения доехали до GitHub :) 

## 6️⃣ Улучшаем API автотест: создание отдельных классов, SoftAssertions и генерация тестовых данных
1. Выносим в отдельные классы: 
* `Requests` все HTTP запросы
* `Specifications` все спецификации
* `CheckedRequests` все проверяемые запросы (проверка статус кода и сериализация ответа в объект)
2. Создаем модели для сериализации ответов запросов из JSON в объект
3. Переписываем тест с использованием новых классов
4. Добавляем Soft Assertions для проверки полей объекта ответа запроса
5. Заменяем захардкоженные тестовые данные на сгенерированные
6. Форматирование всех файлов

Результат урока в ветке [api-test-version-1](https://github.com/Test-Automation-Projects/teamcity-tests-workshop/tree/api-test-version-1), сам тест [BuildConfigurationTest](https://github.com/Test-Automation-Projects/teamcity-tests-workshop/blob/api-test-version-1/src/test/java/api/BuildConfigurationTest.java).


## 7️⃣ Улучшаем API автотест: 
