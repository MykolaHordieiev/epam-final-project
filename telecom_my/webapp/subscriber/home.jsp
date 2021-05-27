<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>JSP file</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <p>
            <lan:print message="subscriber.home.jsp.welcome_home_page"/>, ${sessionScope.user.login}
        </p>
        <p>
             <lan:print message="subscriber.home.jsp.get_info"/>
        </p>
            <form method="GET" action="/telecom/service/subscriber">
                <input type="hidden" name="id" value="${sessionScope.user.id}"/>
                <input type="submit" value='<lan:print message="subscriber.home.jsp.button.get_info"/>'>
            </form>

        <p>
            <lan:print message="subscriber.home.jsp.enter_amount"/>.
        </p>
            <form method="POST" action="/telecom/service/subscriber/balance">
                <input type="number" step="0.01" name="amount"><br><br>
                <input type="submit" value='<lan:print message="subscriber.home.jsp.button.top_up"/>'><br><br>
            </form>
        <p>
            <lan:print message="subscriber.home.jsp.select_subscription"/>.
        </p>
        <form method="GET" action="/telecom/service/get/all/product">
            <input type="submit" value='<lan:print message="subscriber.home.jsp.button.select"/>'><br><br>
        </form>
        <form action="/telecom/service/change/locale" method="POST">
                    <input type="hidden" name="view" value="/subscriber/home.jsp"/>
                                             <select name="selectedLocale">
            <c:forEach var="locale" items="${sessionScope.locales}">
                <option value="${locale}">
                    ${locale}
                </option>
            </c:forEach>
            </select>
            <input type="submit" value='<lan:print message="operator.home.jsp.button.update"/>'>
        </form>
        <form method="GET" action="/telecom">
            <input type="submit" value='<lan:print message="subscriber.home.jsp.button.logout"/>'><br><br>
        </form>
    </body>
</html>