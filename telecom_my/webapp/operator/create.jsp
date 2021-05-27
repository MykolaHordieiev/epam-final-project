<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>Enter to system</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <lan:print message="operator.create.jsp.message"/>.
        </p>
            <form method="POST" action="/telecom/service/subscriber">
              <label for="name"><lan:print message="operator.create.jsp.label.login"/>:</label><br>
              <input type="text" id="login" name="login"><br><br>
              <label for="pass"><lan:print message="operator.create.jsp.label.password"/>:</label><br>
              <input type="password" id="password" name="password"><br><br>
              <input type="submit" value='<lan:print message="operator.create.jsp.button.create"/>'>
            </form>
            <form method="GET" action="/telecom/operator/home.jsp">
                                  <input type="submit" value='<lan:print message="rate.unusable.jsp.button.home"/>'>
                              </form>

    </body>
</html>