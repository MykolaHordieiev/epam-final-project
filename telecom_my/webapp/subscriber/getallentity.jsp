<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Refactor rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            Choose the product and rate of product.
        </p>
        <form method="POST" action="/telecom/service/subscriber/addsubscribing">
            <label for="name">Enter id of product:</label><br>
            <input type="text" id="idOfProduct" name="idOfProduct"><br><br>
            <label for="name">Enter id of rate:</label><br>
            <input type="text" id="idOfRate" name="idOfRate"><br><br>
            <input type="submit" value="add subscribing"><br><br>
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