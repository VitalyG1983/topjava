<%--
  Created by IntelliJ IDEA.
  User: vitalygavrilov
  Date: 20.06.2022
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>i18n</title>

</head>
<body>
<script type="text/javascript">
    const i18n = [];
    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"}%>'>
         i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
    i18n["addTitle"] = '<spring:message code="${param.addTitle}"/>';
    i18n["editTitle"] = '<spring:message code="${param.editTitle}"/>';
</script>
</body>
</html>
