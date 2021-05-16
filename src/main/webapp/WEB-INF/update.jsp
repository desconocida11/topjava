<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>

    <title>Update Meal</title>
</head>
<body>
<h2>Update Meal "${meal.description}"</h2>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime:</dt>
            <dd><label>
                <input type="datetime-local" name="datetime" value="${meal.dateTime}" required>
            </label></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><label>
                <input type="text" name="description" size="50" value="${meal.description}" required>
            </label>
            </dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><label>
                <input type="number" name="calories" size="30" value="${meal.calories}" min="1">
            </label>
            </dd>
        </dl>
        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>
