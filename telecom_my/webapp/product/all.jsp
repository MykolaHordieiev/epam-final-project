<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Refactor rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <table id="products">
            <tr>
                <th>Name</th>
            </tr>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td>${product.name}</td>
                    <td>
                        <form method="GET" action="/telecom/service/rate/product">
                            <input type="hidden" name="productId" value="${product.id}"/>
                            <input type="submit" value="rates" />
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">
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