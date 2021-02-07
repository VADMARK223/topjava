<%--
  User: v.markitanov
  Date: 07.02.2021
--%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<html>
<head>
    <title>Meal card</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>${meal.id == -1 ? 'Create new meal' : 'Edit meal'}</h3>

<form action="meal-card" method="post">
    <input name="id" type="hidden" value="${meal.id}">

    <label for="datetime">Date time:</label>
    <input name="datetime" id="datetime" type="datetime-local" value="${meal.dateTime}">
    <br>

    <label for="description">Description:</label>
    <input name="description" id="description" type="text" value="${meal.description}">
    <br>

    <label for="calories">Calories:</label>
    <input name="calories" id="calories" type="number" value="${meal.calories}">
    <br>

    <input type="submit" value="Save">
    <input type="button" value="Cancel">
</form>
</body>
</html>
