<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JSP file</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            Welcome to home page, ${sessionScope.user.login}
        </p>
        <p>
            For top up balance, please, enter the amount of money.
        </p>
            <form method="POST" action="/telecom/service/subscriber/balance">
                <input type="text" id="amount" name="amount"><br><br>
                <input type="submit" value="top up"><br><br>
            </form>
        <p>
            Press the button to add some product.
        </p>
        <form method="GET" action="/telecom/service/subscriber/getallentity">
            <input type="submit" value="add"><br><br>
        </form>
    </body>
</html>