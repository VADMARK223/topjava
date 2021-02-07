<%--
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
    </style>
    <script>
        function deleteMeal(id) {
            console.log("Delete meal #" + id);
            const xhr = new XMLHttpRequest();
            xhr.open("DELETE", "meals");
            xhr.onreadystatechange = function () {
                if (xhr.status === 200) {
                    location.reload();
                }
            };
            const request = {"id": id};
            xhr.send(JSON.stringify(request));
        }
    </script>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<a href="meal-card">Add meal</a>

<table style="border: 1px solid">
    <thead>
    <tr style="border: 1px solid">
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="meal" items="${meals}">
        <tr style="color: ${meal.excess ? 'red' : 'green'}">
            <td>
                    ${meal.formattedDateTime}
            </td>
            <td>
                    ${meal.description}
            </td>
            <td>
                    ${meal.calories}
            </td>
            <td>
                <form action="meal-card" method="get">
                    <button class="btn-link" type="submit" name="id" value="${meal.id}">Update</button>
                </form>
            </td>
            <td>
                <a onclick="deleteMeal(${meal.id});">
                    <span>
                        <button>Delete</button>
                    </span>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


</body>
</html>
