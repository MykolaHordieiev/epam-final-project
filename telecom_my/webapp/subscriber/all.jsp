<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" %>
<html>
    <head>
        <title>get all subscribers</title>
        <meta charset="UTF-8">
    </head>
    <body>
        <style>
        caption {
          font-family: annabelle;
          font-weight: bold;
          font-size: 1.5em;
          padding: 10px;
          border: 1px solid #A9E2CC;
         }
        th {
          padding: 10px;
          border: 1px solid #A9E2CC;
        }
        td {
          font-size: 1.0em;
          padding: 5px 7px;
          border: 1px solid #A9E2CC;
        }
        </style>
        <table id="subscribers">
        <caption><lan:print message="subscriber.all.jsp.table.caption"/></caption>
             <tr>
                <th><lan:print message="subscriber.all.jsp.table.login"/></th>
                <th><lan:print message="subscriber.all.jsp.table.balance"/></th>
                <th><lan:print message="subscriber.all.jsp.table.status"/></th>
                <th><lan:print message="subscriber.all.jsp.table.lock"/></th>
             </tr>
             <c:forEach items="${subscribers}" var="subscriber">
                <tr>
                    <td>${subscriber.login}</td>
                     <td>
                        <fmt:setLocale value = "en_US"/>
                        <fmt:formatNumber value="${subscriber.balance}" type="currency"/>
                    </td>
                    <td>${subscriber.lock}</td>
                    <td>
                        <c:if test = "${subscriber.lock == 'false'}">
                            <form method="POST" action="/telecom/service/subscriber/lock">
                                <input type="hidden" name="id" value="${subscriber.id}"/>
                                <input type="submit" value='<lan:print message="subscriber.all.jsp.button.lock"/>'>
                            </form>
                        </c:if>
                        <c:if test = "${subscriber.lock == 'true'}">
                            <form method="POST" action="/telecom/service/subscriber/unlock">
                                <input type="hidden" name="id" value="${subscriber.id}"/>
                                <input type="submit" value='<lan:print message="subscriber.all.jsp.button.unlock"/>'>
                            </form>
                        </c:if>
                    </td>
                </tr>
             </c:forEach>
        </table>
        <c:if test = "${countOfHref != 0}">
            <c:forEach var = "i" begin = "1" end = "${countOfHref}">
                <a href="/telecom/service/subscriber/all?page=${i}"> ${i} </a>
            </c:forEach>
        </c:if>
        <form method="GET" action="/telecom/operator/home.jsp">
            <input type="submit" value='<lan:print message="subscriber.all.jsp.button.home"/>'>
        </form>
    </body>
</html>