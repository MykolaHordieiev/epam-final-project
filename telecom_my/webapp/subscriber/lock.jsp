<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>subscriber by id</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <p>
        Subscribing was add. But you are locked until you replenish your balance.
    </p>
    <p>
        To get info about balance.
    </p>
    <form method="GET" action="/telecom/service/subscriber">
        <input type="hidden" name="id" value="${sessionScope.user.id}"/>
        <input type="submit" value="Get info">
    </form>
    <form method="GET" action="/telecom/subscriber/home.jsp">
        <input type="submit" value="Home">
    </form>
    </body>
</html>