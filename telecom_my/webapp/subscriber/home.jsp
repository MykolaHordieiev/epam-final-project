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
             Get info about account.
        </p>
            <form method="GET" action="/telecom/service/subscriber">
                <input type="hidden" name="id" value="${sessionScope.user.id}"/>
                <input type="submit" value="Get info">
            </form>

        <p>
            Enter the amount of money for top up balance.
        </p>
            <form method="POST" action="/telecom/service/subscriber/balance">
                <input type="text" id="amount" name="amount"><br><br>
                <input type="submit" value="Top up"><br><br>
            </form>
        <p>
            Click the button to select a subscription.
        </p>
        <form method="GET" action="/telecom/service/get/all/product">
            <input type="submit" value="Select"><br><br>
        </form>
        <form method="GET" action="/telecom">
            <input type="submit" value="Logout"><br><br>
        </form>
    </body>
</html>