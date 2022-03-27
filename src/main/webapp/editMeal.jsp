<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal.id==null ? "Добавить еду" : "Редактировать еду"}
</h2>
<br/>
<section>
    <form name="editForm" method="post" action="meals"
          enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        </h3>
        <dl>
            <dt>Дата/Время</dt>
            <dd><input type="datetime-local" name="dateTime" size=30 value="${meal.dateTime}" required></dd>
            <dt>Описание</dt>
            <dd><input type="text" name="description" size=30 value="${meal.description}" required
                       title="'Описание' не должно быть пустым и содержать специальных символов"></dd>
            <dt>Калории</dt>
            <dd><input type="number" name="calories" size=30 value="${meal.calories}" required></dd>
        </dl>
        <p></p>
        <button style="margin-left: 20px" type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back();return false">Отменить</button>
    </form>
</section>
</body>
</html>