<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-dark bg-dark py-0">
    <div class="container">
        <a href="meals" class="navbar-brand"><img src="resources/images/icon-meal.png"> <spring:message
                code="app.title"/></a>
        <sec:authorize access="isAuthenticated()">
            <form:form class="form-inline my-2" action="logout" method="post">
                <sec:authorize access="hasRole('ADMIN')">
                    <a class="btn btn-info mr-1" href="users"><spring:message code="user.title"/></a>
                </sec:authorize>
                <a class="btn btn-info mr-1" href="profile">${userTo.name} <spring:message code="app.profile"/></a>
                <button class="btn btn-primary my-1" type="submit">
                    <span class="fa fa-sign-out"></span>
                </button>
            </form:form>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            <form:form class="form-inline my-2" id="login_form" action="spring_security_check" method="post">
                <input class="form-control mr-1" type="text" placeholder="Email" name="username">
                <input class="form-control mr-1" type="password" placeholder="Password" name="password">
                <button class="btn btn-success" type="submit">
                    <span class="fa fa-sign-in"></span>
                </button>
            </form:form>
        </sec:authorize>

        <c:set var="uri" value='<%=request.getAttribute("javax.servlet.forward.request_uri")%>'/>
        <c:set var="currentLang" value='${pageContext.response.locale}'/>
        <input type="hidden" id="currLang" value="${currentLang}">
        <spring:message code="app.lang"/>: <select id="language" name="language" onchange="location = this.value;">
        <option value="${uri}?lang=en">English</option>
        <option value="${uri}?lang=ru">Русский</option>
    </select>
    </div>

    <script type="text/javascript">
        $('select[name="language"]').val("${uri}?lang=" + "${currentLang}");
    </script>
</nav>
