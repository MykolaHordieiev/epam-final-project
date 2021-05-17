<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>get all subscribers</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <c:forEach items="${subscribers}" var="subscriber">
                <p>subscribers id: ${subscriber.id} login: ${subscriber.login} balance: ${subscriber.balance}
                 locked: ${subscriber.lock}</p>
        </c:forEach>
    </body>
</html>