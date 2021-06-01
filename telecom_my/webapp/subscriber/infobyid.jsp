<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
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
        <caption><lan:print message="subscriber.infobyid.jsp.table.caption"/></caption>
        <tr>
            <th><lan:print message="subscriber.infobyid.jsp.table.login"/></th>
            <th><lan:print message="subscriber.infobyid.jsp.table.balance"/></th>
            <th><lan:print message="subscriber.infobyid.jsp.table.status"/></th>
        </tr>
        <tr>
            <td>${subscriber.login}</td>
            <td>${subscriber.balance}</td>
            <td>
                <c:if test = "${subscriber.lock == 'true'}"><lan:print message="subscriber.infobyid.jsp.table.status.lock"/></c:if>
                <c:if test = "${subscriber.lock == 'false'}"><lan:print message="subscriber.infobyid.jsp.table.status.unlock"/></c:if>
            </td>
        </tr>

        <c:if test = "${subscriptions.isEmpty()}">
            <tr>
                <td colspan="3"><lan:print message="subscriber.infobyid.jsp.table.no_subscriptions"/></td>
            </tr>
        </c:if>
        <c:if test = "${!subscriptions.isEmpty()}">
        <tr>
            <td colspan="3"><lan:print message="subscriber.infobyid.jsp.table.subscribing"/></td>
        </tr>
        <tr>
            <td ><lan:print message="subscriber.infobyid.jsp.table.product_name"/></td>
            <td colspan="2"><lan:print message="subscriber.infobyid.jsp.table.rate_name"/></td>
        </tr>

        <tr>
        <c:forEach items="${subscriptions}" var="subscription">
            <td >${subscription.product.name}</td>
            <td colspan="2">${subscription.rate.name}</td>
        </tr>
        </c:forEach>
        </c:if>
    </table>

        <c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">
            <c:if test = "${subscriber.lock == 'false'}">
            <form method="POST" action="/telecom/service/subscriber/lock">
                <input type="hidden" name="id" value="${subscriber.id}"/>
                <input type="submit" value='<lan:print message="subscriber.infobyid.jsp.button.lock"/>'>
            </form>
            </c:if>
            <c:if test = "${subscriber.lock == 'true'}">
            <form method="POST" action="/telecom/service/subscriber/unlock">
                <input type="hidden" name="id" value="${subscriber.id}"/>
                <input type="submit" value='<lan:print message="subscriber.infobyid.jsp.button.unlock"/>'>
            </form>
            </c:if>
            <form method="GET" action="/telecom/operator/home.jsp">
                <input type="submit" value='<lan:print message="subscriber.infobyid.jsp.button.home"/>'>
            </form>
        </c:if>
        <c:if test = "${sessionScope.user.userRole == 'SUBSCRIBER'}">
            <form method="GET" action="/telecom/subscriber/home.jsp">
                <input type="submit" value='<lan:print message="subscriber.infobyid.jsp.button.home"/>'>
            </form>
        </c:if>
    </body>
</html>