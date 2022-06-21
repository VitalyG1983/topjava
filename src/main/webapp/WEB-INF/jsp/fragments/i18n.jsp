<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<title>i18n</title>

<script type="text/javascript">
    const i18n = [];
    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"}%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
    i18n["addTitle"] = '<spring:message code="${param.addTitle}"/>';
    i18n["editTitle"] = '<spring:message code="${param.editTitle}"/>';
    i18n["title"] = '<spring:message code="${param.title}"/>';
    i18n["thDateTime"] = '<spring:message code="${param.thDateTime}"/>';
    i18n["thName"] = '<spring:message code="${param.thName}"/>';
</script>
</html>
