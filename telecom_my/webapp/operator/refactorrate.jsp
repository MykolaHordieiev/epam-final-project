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
            <form method="POST" action="/telecom/service/operator/addrate">
              <label for="name">name of rate:</label><br>
              <input type="text" id="name" name="name"><br><br>
              <label for="name">price of rate:</label><br>
              <input type="text" id="price" name="price"><br><br>
              <label for="name">id of product:</label><br>
              <input type="text" id="id" name="id"><br><br>
              <input type="submit" value="add">
            </form>

            <form method="POST" action="/telecom/service/operator/deleterate">
                <label for="name">id of product:</label><br>
                <input type="text" id="id" name="id"><br><br>
                <input type="submit" value="delete">
            </form>

            <p>
                 Please, write id Of rate which you want change
            </p>
            <form method="POST" action="/telecom/service/operator/changerate">
                            <label for="name">id of rate:</label><br>
                            <input type="text" id="id" name="id"><br><br>
                            <label for="name">name if you want change name</label><br>
                            <input type="text" id="name" name="name"><br><br>
                            <label for="name">price if you want c price:</label><br>
                            <input type="text" id="price" name="price"><br><br>
                            <input type="submit" value="change">
            </form>

    </body>
</html>