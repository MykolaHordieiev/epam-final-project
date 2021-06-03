<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>Products</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <table id="products">
            <tr>
                <th><lan:print message="product.all.jsp.table.caption"/></th>
            </tr>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td>${product.name}</td>
                    <td>
                        <form method="GET" action="/telecom/service/rate/product">
                            <input type="hidden" name="productId" value="${product.id}"/>
                            <input type="submit" value='<lan:print message="product.all.jsp.button.rates"/>'/>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">
            <form method="GET" action="/telecom/operator/home.jsp">
                <input type="submit" value='<lan:print message="product.all.jsp.button.home"/>'>
            </form>
        </c:if>
        <c:if test = "${sessionScope.user.userRole == 'SUBSCRIBER'}">
            <form method="GET" action="/telecom/subscriber/home.jsp">
                <input type="submit" value='<lan:print message="product.all.jsp.button.home"/>'>
            </form>
        </c:if>
    </body>
</html>