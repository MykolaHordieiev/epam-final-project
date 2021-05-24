<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>unusable rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p> <lan:print message="rate.unusable.jsp.rate"/>: ${rate} <lan:print message="rate.unusable.jsp.used"/>:
        <c:forEach items="${subscribers}" var="subscriber">
            ${subscriber.login},
        </c:forEach>
        </p>
        <p> <lan:print message="rate.unusable.jsp.system"/>.</p>
        <p> <lan:print message="rate.unusable.jsp.information"/>:
            <lan:print message="rate.byid.jsp.label.name"/>: ${rate.name},
            <lan:print message="rate.byid.jsp.unusable"/>: ${rate.unusable}
        </p>
        <form method="GET" action="/telecom/operator/home.jsp">
            <input type="submit" value='<lan:print message="rate.unusable.jsp.button.home"/>'>
        </form>
    </body>
</html>