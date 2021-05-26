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
            <lan:print message="operator.home.jsp.welcome_home_page"/>, ${sessionScope.user.login}
        </p>
             <form method="POST" action="/telecom/operator/create.jsp">
                <label><lan:print message="operator.home.jsp.create_subscriber"/></label><br>
                    <input type="submit" value='<lan:print message="operator.home.jsp.button.create"/>'>
             </form>
        <p>
            <lan:print message="operator.home.jsp.get_subscriber"/>.
        </p>
               <form method="GET" action="/telecom/service/subscriber">
                    <label for="id"><lan:print message="operator.home.jsp.label.get_by_id"/></label><br>
                    <input type="text" id="id" name="id"><br>
                    <input type="submit" value='<lan:print message="operator.home.jsp.button.Get_info"/>'>
               </form>
        <p>
             <lan:print message="operator.home.jsp.get_all_subscriber"/>.
        </p>
                <form method="GET" action="/telecom/service/subscriber/all">
                        <input type="submit" value='<lan:print message="operator.home.jsp.button.Get_info"/>'>
                </form>
        <p>
            <lan:print message="operator.home.jsp.get_info_about_all_product"/>.
        </p>
            <form method="GET" action="/telecom/service/get/all/product">
                <input type="submit" value='<lan:print message="operator.home.jsp.button.Get_info"/>'><br><br>
            </form>
            <form method="POST" action="/telecom/service/logout">
                <input type="submit" value='<lan:print message="operator.home.jsp.button.Logout"/>'><br><br>
            </form>
            <form action="/telecom/service/change/locale" method="POST">
            <input type="hidden" name="view" value="/operator/home.jsp"/>
                                     <select name="selectedLocale">
                                         <c:forEach var="locale" items="${sessionScope.locales}">
                                             <option value="${locale}">
                                                 ${locale}
                                             </option>
                                         </c:forEach>
                                     </select>
                                     <input type="submit" value='<lan:print message="operator.home.jsp.button.update"/>'>
            </form>
    </body>
</html>