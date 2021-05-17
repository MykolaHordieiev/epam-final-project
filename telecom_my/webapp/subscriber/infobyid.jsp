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
    </body>
</html>