<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>subscriber by id</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <jsp:text>
               subscriber id: ${subscriber.id} login: ${subscriber.login}
               subscriber balance: ${subscriber.balance} locked: ${subscriber.lock}
            </jsp:text>
        </p>
        <c:forEach items="${subscriptions}" var="subscription">
            <p>product name: ${subscription.product} rate name: ${subscription.rate}</p>
        </c:forEach>

        <c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">
            <c:if test = "${subscriber.lock == 'false'}">
            <form method="POST" action="/telecom/service/subscriber/lock">
                <input type="hidden" name="id" value="${subscriber.id}"/>
                <input type="submit" value="lock">
            </form>
            </c:if>
            <c:if test = "${subscriber.lock == 'true'}">
            <form method="POST" action="/telecom/service/subscriber/unlock">
                <input type="hidden" name="id" value="${subscriber.id}"/>
                <input type="submit" value="unlock">
            </form>
            </c:if>
            <form method="GET" action="/telecom/operator/home.jsp">
                <input type="submit" value="go home">
            </form>
        </c:if>
        <c:if test = "${sessionScope.user.userRole == 'SUBSCRIBER'}">
            <form method="GET" action="/telecom/subscriber/home.jsp">
                <input type="submit" value="go home">
            </form>
        </c:if>
    </body>
</html>