<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="../index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<section>
    <a href="meals?action=update">Add Meal</a>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <jsp:useBean id="meals" scope="request" type="java.util.List"/>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <c:set var="color" value="green"/>
            <c:if test="${meal.excess == true}"><c:set var="color" value="red"/></c:if>
            <tr style="color: ${color}">
                <td><%=meal.getDateTime()
                        .format(DateTimeFormatter.ofPattern("H:mm:ss yyyy-MM-d"))%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?id=${meal.id}&action=update">Update</a></td>
                <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>