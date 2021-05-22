<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>get all subscribers</title>
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
          font-size: 1.0em;
          padding: 5px 7px;
          border: 1px solid #A9E2CC;
        }
        </style>
        <table id="subscribers">
        <caption>Info about subscribers</caption>
             <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Balance</th>
                <th>Status</th>
                <th>Lock/Unlock</th>
             </tr>
             <c:forEach items="${subscribers}" var="subscriber">
                <tr>
                    <td>${subscriber.id}</td>
                    <td>${subscriber.login}</td>
                    <td>${subscriber.balance}</td>
                    <td>${subscriber.lock}</td>
                    <td>
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
                    </td>
                </tr>
             </c:forEach>
        </table>
        <form method="GET" action="/telecom/operator/home.jsp">
            <input type="submit" value="Home">
        </form>
    </body>
</html>