<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
    <hr>
<h2>Meals</h2>
<section>
    <table style="margin: auto" border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата/Время</th>
            <th>Описание</th>
            <th>Калории</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal"/>
            <tr>
                <td>${meal.dateTime}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meal?id=${meal.id}&action=edit"><img src="img/pencil.png"></a></td>
                <td><a href="meal?id=${meal.id}&action=delete"><img src="img/delete.png"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>