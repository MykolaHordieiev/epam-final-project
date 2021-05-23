<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>unusable rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
      Rate: ${rate} is used by subscribers:
     <c:forEach items="${subscribers}" var="subscriber">
     ${subscriber.login}
     </c:forEach>
     So system change rate usability.
     Information about rate: name ${rate.name} and unusable ${rate.unusable}
     <form method="GET" action="/telecom/operator/home.jsp">
                     <input type="submit" value="Home">
                 </form>
      </body>
    </html>