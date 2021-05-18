<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JSP file</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            Welcome to home page, ${sessionScope.user.login}<br>
            If you want create new subscriber, press the button.
        </p>
             <form method="POST" action="/telecom/operator/create.jsp">
                    <input type="submit" value="create">
             </form>
        <p>
            If you want get subscriber by id, please enter the id.
        </p>
               <form method="GET" action="/telecom/service/subscriber">
                <label for="id">Enter id:</label><br>
                    <input type="text" id="id" name="id"><br><br>
                    <input type="submit" value="get by id">
               </form>
        <p>
             If you want get all subscribers, press the button.
        </p>
                <form method="GET" action="/telecom/service/subscriber/all">
                        <input type="submit" value="get all">
                </form>
        <p>
            To get information about all product.
        </p>
            <form method="GET" action="/telecom/service/operator/getallproduct">
                <input type="submit" value="get all"><br><br>
            </form>
        <p>
             To do change with rates.
        </p>
            <form method="GET" action="/telecom/operator/refactorrate.jsp">
            <input type="submit" value="do change"><br><br>
            </form>
    </body>
</html>