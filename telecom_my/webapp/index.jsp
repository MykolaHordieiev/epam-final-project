<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>Welcome page!</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
           <lan:print message="index.jsp.welcome_index_page"/>
           ${sessionScope.Locale}
        </p>
            <form method="POST" action="/telecom/service/login">
                          <label for="name"><lan:print message="index.jsp.label.login"/></label><br>
                          <input type="text" id="login" name="login"><br><br>
                          <label for="pass"><lan:print message="index.jsp.label.password"/></label><br>
                          <input type="password" id="password" name="password"><br><br>
                          <input type="submit" value='<lan:print message="index.jsp.button.login"/>'>
            </form>
            <form action="index.jsp" method="GET">
                         <select name="Locale">
                             <c:forEach var="locale" items="${sessionScope.locales}">
                                 <option value="${locale}">
                                     ${locale}
                                 </option>
                             </c:forEach>
                         </select>
                         <input type="submit" value="Update">
            </form>
   </body>
</html>