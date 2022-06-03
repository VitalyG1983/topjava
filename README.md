
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/c3caddf623b84c78b06d11d613ecae42)](https://www.codacy.com/gh/Vitaly1983G/topjava/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Vitaly1983G/topjava&amp;utm_campaign=Badge_Grade)

Lesson 07: MealRestController's Curl requests:

getAll():

`curl 'http://localhost:8080/topjava/rest/meals'`

get():

`curl 'http://localhost:8080/topjava/rest/meals/100003'`

update():

`curl  -X PUT -H 'Content-Type: application/json' -d '{"id":"100003", "dateTime":"2020-01-30T10:02", "description":"breakfast updated", "calories":"200"}' http://localhost:8080/topjava/rest/meals/100003`

delete():

`curl -X DELETE 'http://localhost:8080/topjava/rest/meals/100003'`

createWithLocation():

`curl -X POST -H 'Content-Type: application/json' -d '{"id":null, "dateTime":"2020-02-01T18:00", "description":"Созданный ужин", "calories":"300"}' http://localhost:8080/topjava/rest/meals`

fourParamGetBetween():

`curl 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=19:00&endTime=21:00'`

Java Enterprise Online Project 
===============================
Разработка полнофункционального Spring/JPA Enterprise приложения c авторизацией и правами доступа на основе ролей с использованием наиболее популярных инструментов и технологий Java: Maven, Spring MVC, Security, JPA(Hibernate), REST(Jackson), Bootstrap (css,js), datatables, jQuery + plugins, Java 8 Stream and Time API и хранением в базах данных Postgresql и HSQLDB.

![topjava_structure](https://user-images.githubusercontent.com/13649199/27433714-8294e6fe-575e-11e7-9c41-7f6e16c5ebe5.jpg)
