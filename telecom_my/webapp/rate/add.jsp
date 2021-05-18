<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Refactor rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            Please, write info about rate.
        </p>
            <form method="POST" action="/telecom/service/rate/addrate">
              <label for="name">name of rate:</label><br>
              <input type="text" id="name" name="name"><br><br>
              <label for="name">price of rate:</label><br>
              <input type="text" id="price" name="price"><br>
              <input type="hidden" name="productId" value="${productId}"/><br>
              <input type="submit" value="add">
            </form>
    </body>
</html>