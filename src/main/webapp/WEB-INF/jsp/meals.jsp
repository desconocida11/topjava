<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/topjava.common.js" defer></script>
<script type="text/javascript" src="resources/js/topjava.meals.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3><spring:message code="meal.title"/></h3>
        <form id="filterForm">
            <div class="form-group row">
                <label for="startDate" class="col-sm-2 col-form-label"><spring:message code="meal.startDate"/></label>
                <div class="col-sm-10">
                    <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}"
                           placeholder="<spring:message code="meal.startDate"/>">
                </div>
            </div>

            <div class="form-group row">
                <label for="endDate" class="col-sm-2 col-form-label"><spring:message code="meal.endDate"/></label>
                <div class="col-sm-10">
                    <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}"
                           placeholder="<spring:message code="meal.endDate"/>">
                </div>
            </div>

            <div class="form-group row">
                <label for="startTime" class="col-sm-2 col-form-label"><spring:message code="meal.startTime"/></label>
                <div class="col-sm-10">
                    <input type="time" class="form-control" id="startTime" name="startTime" value="${param.startTime}"
                           placeholder="<spring:message code="meal.startTime"/>">
                </div>
            </div>

            <div class="form-group row">
                <label for="endTime" class="col-sm-2 col-form-label"><spring:message code="meal.endTime"/></label>
                <div class="col-sm-10">
                    <input type="time" class="form-control" id="endTime" name="endTime" value="${param.endTime}"
                           placeholder="<spring:message code="meal.endTime"/>">
                </div>
            </div>
        </form>
        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="cancelFilter()">
            <span class="fa fa-close"></span>
            <spring:message code="meal.cancelFilter"/>
        </button>
        <button type="button" class="btn btn-primary" onclick="filter()">
            <span class="fa fa-check"></span>
            <spring:message code="meal.filter"/>
        </button>
        <hr>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
        </button>
        <table class="table table-striped" id="mealtable">
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                <tr data-mealExcess="${meal.excess}" id="${meal.id}">
                    <td>
                            <%--                            <c:out value="${fn:formatDateTime(meal.dateTime)}"/>--%>
                            <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                            <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                            <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                            ${fn:formatDateTime(meal.dateTime)}
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="meals/update?id=${meal.id}"><spring:message code="common.update"/></a></td>
                        <%--                        <td><a href="meals/delete?id=${meal.id}"><spring:message code="common.delete"/></a></td>--%>

                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" name="id" value="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.dateTime"/></label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime"
                               placeholder="<spring:message code="meal.dateTime"/>" required>
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message
                                code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>" required>
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories"
                               placeholder="<spring:message code="meal.calories"/>" required>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>