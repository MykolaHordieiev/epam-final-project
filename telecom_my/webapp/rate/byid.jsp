<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>subscriber by id</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <c:if test = "${rate.unusable == 'false'}">
        <form method="POST" action="/telecom/service/rate">
                      <label for="name">Name:</label>
                      <input type="text" id="name" name="name" value="${rate.name}">
                      <label for="price">Price:</label>
                      <input type="number" id="price" name="price" value="${rate.price}">
                      <input type="hidden" name="method" value="PUT">
                      <input type="hidden" name="id" value="${rate.id}">
                      <input type="hidden" name="productId" value="${rate.productId}">
                      <input type="submit" value="Update">
        </form>
        <form method="POST" action="/telecom/service/rate">
                 <input type="hidden" name="method" value="DELETE">
                 <input type="hidden" name="id" value="${rate.id}">
                 <input type="hidden" name="productId" value="${rate.productId}">
                 <input type="submit" value="Delete">
        </form>
        </c:if>
        <c:if test = "${rate.unusable == 'true'}">
        <p>Rate info:</p>
        name ${rate.name} price: ${rate.price} unusable: ${rate.unusable}
        </c:if>
    </body>
</html>