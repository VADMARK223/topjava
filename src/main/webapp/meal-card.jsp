<%--
  User: v.markitanov
  Date: 07.02.2021
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Meal card</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3>Card meal</h3>
<form action="meal-card" method="post">
    Date time:
    <label for="datetime"></label>
    <input name="datetime" id="datetime" type="datetime-local">
    <br>

    Description:
    <label for="description"></label>
    <input name="description" id="description" type="text">
    <br>

    Calories:
    <label for="calories"></label>
    <input name="calories" id="calories" type="number">
    <br>
    <input type="submit" value="Save">
    <input type="button" value="Cancel">
</form>
</body>
</html>
