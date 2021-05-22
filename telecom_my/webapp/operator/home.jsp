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
             <form method="POST" action="/telecom/operator/create.jsp">
                <label>Create new subscriber</label><br>
                    <input type="submit" value="Create">
             </form>
        <p>
            To get info about subscriber.
        </p>
               <form method="GET" action="/telecom/service/subscriber">
                    <label for="id">Enter id:</label><br>
                    <input type="text" id="id" name="id"><br>
                    <input type="submit" value="Get info">
               </form>
        <p>
             To get info about all subscribers.
        </p>
                <form method="GET" action="/telecom/service/subscriber/all">
                        <input type="submit" value="Get info">
                </form>
        <p>
            To get info about all product.
        </p>
            <form method="GET" action="/telecom/service/get/all/product">
                <input type="submit" value="Get info"><br><br>
            </form>
            <form method="GET" action="/telecom/service/logout">
                <input type="submit" value="Logout"><br><br>
            </form>
    </body>
</html>