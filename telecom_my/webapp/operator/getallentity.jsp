<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Refactor rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <p>
         To do change with rates.
    </p>
    <form method="GET" action="/telecom/operator/refactorrate.jsp">
                <input type="submit" value="do change"><br><br>
    </form>
        <table id="products">
                  <tr>
                    <th>Name</th>
                  </tr>
                  <c:forEach items="${products}" var="product">
                  <tr>
                    <td>${product.name}</td>
                    <td>
                        <form method="GET" action="/telecom/service/rate/product">
                            <input type="hidden" name="id" value="${product.id}"/>
                            <input type="submit" value="rates" />
                        </form>
                    </td>
                  </tr>
                  </c:forEach>
                </table>
    </body>
</html>