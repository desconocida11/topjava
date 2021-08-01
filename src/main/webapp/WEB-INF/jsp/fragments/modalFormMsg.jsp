<%@ page import="ru.javawebinar.topjava.util.Util" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    <c:forEach var="key" items='<%=Util.getCommonAppMsg()%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>