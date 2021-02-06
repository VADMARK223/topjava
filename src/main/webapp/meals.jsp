<jsp:useBean id="username" scope="request" type="java.lang.String"/>
<%--
  User: v.markitanov
  Date: 06.02.2021
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
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
    </style>
    <script>
        function deleteMeal(id) {
            console.log("Delete meal #" + id);
            const xhr = new XMLHttpRequest();
            const url = "http://localhost:8080/topjava/meals";
            xhr.open("DELETE", url + "?id=" + id);
            xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xhr.onreadystatechange = function () {
                if(xhr.status === 200) {
                    location.reload();
                }
            };
            xhr.send(null);
        }
    </script>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h1>Hello, ${username}</h1>
<jsp:useBean id="meals" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>

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
        <tr>
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
                <form id="form-update-${meal.id}" action="meals?id=${meal.id}" method="post">
                    <a href="javascript:" onclick="document.getElementById('form-update-${meal.id}').submit();">Update</a>
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
