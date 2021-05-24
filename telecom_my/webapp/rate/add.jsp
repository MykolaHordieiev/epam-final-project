<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>Refactor rate</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
           <lan:print message="rate.add.jsp.write_info"/>
        </p>
            <form method="POST" action="/telecom/service/rate/add">
              <label for="name"><lan:print message="rate.add.jsp.label.name"/>:</label><br>
              <input type="text" id="name" name="name"><br><br>
              <label for="name"><lan:print message="rate.add.jsp.label.price"/>:</label><br>
              <input type="text" id="price" name="price"><br>
              <input type="hidden" name="productId" value="${productId}"/><br>
              <input type="submit" value='<lan:print message="rate.add.jsp.button.add"/>'>
            </form>
    </body>
</html>