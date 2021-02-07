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
<h2>${meal.id == -1 ? 'Create new meal' : 'Edit meal'}</h2>

<form action="meal-card" method="post">
    <input name="id" type="hidden" value="${meal.id}">

    <table>
        <tbody>
        <tr>
            <td>
                <label for="datetime">Date time:</label>
            </td>
            <td>
                <input name="datetime" id="datetime" type="datetime-local" value="${meal.dateTime}">
            </td>
        </tr>

        <tr>
            <td>
                <label for="description">Description:</label>
            </td>
            <td>
                <input name="description" id="description" type="text" value="${meal.description}">
            </td>
        </tr>

        <tr>
            <td>
                <label for="calories">Calories:</label>
            </td>
            <td>
                <input name="calories" id="calories" type="number" value="${meal.calories}">
            </td>
        </tr>

        <tr>
            <td>
                <input type="submit" value="Save">
                <input type="button" onclick="window.history.back()" value="Cancel">
            </td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>
