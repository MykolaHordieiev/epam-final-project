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
             If you want get info about account.
        </p>
            <form method="GET" action="/telecom/service/subscriber">
                <input type="hidden" name="id" value="${sessionScope.user.id}"/>

                <input type="submit" value="get info">
            </form>

        <p>
            For top up balance, please, enter the amount of money.
        </p>
            <form method="POST" action="/telecom/service/subscriber/balance">
                <input type="text" id="amount" name="amount"><br><br>
                <input type="submit" value="top up"><br><br>
            </form>
        <p>
            Click the button to select a subscription.
        </p>
        <form method="GET" action="/telecom/service/get/all/product">
            <input type="submit" value="add"><br><br>
        </form>
        <form method="GET" action="/telecom">
            <input type="submit" value="log out"><br><br>
        </form>
    </body>
</html>