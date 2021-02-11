<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %><%--
  User: v.markitanov
  Date: 06.02.2021
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
        }

        td, th {
            border: 1px solid black;
            text-align: center;
            padding: 5px;
        }

        .btn-link {
            border: none;
            outline: none;
            background: none;
            cursor: pointer;
            color: #0000EE;
            padding: 0;
            text-decoration: underline;
            font-family: inherit;
            font-size: inherit;
        }

        form {
            margin-bottom: 0;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<form action="meals" method="get">
    <button class="btn-link" type="submit" name="action" value="card">Add meal</button>
</form>

<table style="border: 1px solid">
    <thead>
    <tr style="border: 1px solid">
        <th style="width: 140px; align-items: center">Date</th>
        <th style="width: 300px">Description</th>
        <th style="width: 80px">Calories</th>
        <th style="width: 60px"></th>
        <th style="width: 60px"></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="meal" items="${meals}">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td>
                <%=meal.getDateTime().format(TimeUtil.formatter)%>
            </td>
            <td style="text-align: left;">
                    ${meal.description}
            </td>
            <td>
                    ${meal.calories}
            </td>
            <td>
                <form action="meals" method="get">
                    <input type="hidden" name="action" value="card">
                    <button class="btn-link" type="submit" name="id" value="${meal.id}">Update</button>
                </form>
            </td>
            <td>
                <form action="meals" method="post">
                    <input type="hidden" name="action" value="delete">
                    <button class="btn-link" type="submit" name="id" value="${meal.id}">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


</body>
</html>
