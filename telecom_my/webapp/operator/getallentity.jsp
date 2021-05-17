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
        <c:forEach items="${products}" var="product">
               <p>products id: ${product.id} product_name: ${product.name}</p>
         </c:forEach>

         <c:forEach items="${rates}" var="rate">
                        <p>rate id: ${rate.id} rate_name: ${rate.name} price: ${rate.price}
                        product_id: ${rate.product_id}</p>
         </c:forEach>

    </body>
</html>