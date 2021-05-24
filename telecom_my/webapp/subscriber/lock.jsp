<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>subscriber by id</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <p>
        <lan:print message="subscriber.lock.jsp.subscribing_was_add"/>.
        <lan:print message="subscriber.lock.jsp.you_are_lock"/>.
    </p>
    <p>
        <lan:print message="subscriber.lock.jsp.get_info_balance"/>.
    </p>
    <form method="GET" action="/telecom/service/subscriber">
        <input type="hidden" name="id" value="${sessionScope.user.id}"/>
        <input type="submit" value='<lan:print message="subscriber.lock.jsp.button.get_info"/>'>
    </form>
    <form method="GET" action="/telecom/subscriber/home.jsp">
        <input type="submit" value='<lan:print message="subscriber.lock.jsp.button.home"/>'>
    </form>
    </body>
</html>