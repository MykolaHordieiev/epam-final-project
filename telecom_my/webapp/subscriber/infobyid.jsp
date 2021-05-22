<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>subscriber info</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <style>
    caption {
      font-family: annabelle;
      font-weight: bold;
      font-size: 1.5em;
      padding: 10px;
      border: 1px solid #A9E2CC;
     }
    th {
      padding: 10px;
      border: 1px solid #A9E2CC;
    }
    td {
      font-size: 1.1em;
      padding: 5px 7px;
      border: 1px solid #A9E2CC;
    }
    </style>
    <table id="subscriberInfo">
        <caption>Info about subscriber</caption>
        <tr>
            <th>Id</th>
            <th>Login</th>
            <th>Balance</th>
            <th>Status</th>
        </tr>
        <tr>
            <td>${subscriber.id}</td>
            <td>${subscriber.login}</td>
            <td>${subscriber.balance}</td>
            <td>${subscriber.lock}</td>
        </tr>
        <tr>
            <td colspan="4">Subscribing</td>
        </tr>
        <tr>
            <td colspan="2">Product name</td>
            <td colspan="2">Rate name</td>
        </tr>
        <tr>
        <c:forEach items="${subscriptions}" var="subscription">
            <td colspan="2">${subscription.product}</td>
            <td colspan="2">${subscription.rate}</td>
        </tr>
        </c:forEach>
    </table>

        <c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">
            <c:if test = "${subscriber.lock == 'false'}">
            <form method="POST" action="/telecom/service/subscriber/lock">
                <input type="hidden" name="id" value="${subscriber.id}"/>
                <input type="submit" value="Lock">
            </form>
            </c:if>
            <c:if test = "${subscriber.lock == 'true'}">
            <form method="POST" action="/telecom/service/subscriber/unlock">
                <input type="hidden" name="id" value="${subscriber.id}"/>
                <input type="submit" value="Unlock">
            </form>
            </c:if>
            <form method="GET" action="/telecom/operator/home.jsp">
                <input type="submit" value="Home">
            </form>
        </c:if>
        <c:if test = "${sessionScope.user.userRole == 'SUBSCRIBER'}">
            <form method="GET" action="/telecom/subscriber/home.jsp">
                <input type="submit" value="Home">
            </form>
        </c:if>
    </body>
</html>