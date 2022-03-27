<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<button type="button" OnClick="location.href='meals?action=newmeal'">Add meal</button>
<section>
    <br>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <c:forEach var="meal" items="${meals}">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${meal.excess ? 'meals:red' : 'meals:green'}">
                <td>${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()} </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?id=${meal.id}&action=edit"><img src="img/pencil.png"></a></td>
                <td><a href="meals?id=${meal.id}&action=delete"><img src="img/delete.png"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>