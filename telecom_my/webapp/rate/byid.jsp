<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/language.tld" prefix="lan" %>
<html>
    <head>
        <title>rate info</title>
        <meta charset="UTF-8">
    </head>
    <body>
    <c:if test = "${rate.unusable == 'false'}">
        <form method="POST" action="/telecom/service/rate">
                      <label for="name"><lan:print message="rate.byid.jsp.label.name"/>:</label>
                      <input type="text" id="name" name="name" value="${rate.name}">
                      <label for="price"><lan:print message="rate.byid.jsp.label.price"/>:</label>
                      <input type="number" step="0.01" name="price" value="${rate.price}">
                      <input type="hidden" name="method" value="PUT">
                      <input type="hidden" name="id" value="${rate.id}">
                      <input type="hidden" name="productId" value="${rate.productId}">
                      <input type="submit" value='<lan:print message="rate.byid.jsp.button.update"/>'>
        </form>
        <form method="POST" action="/telecom/service/rate">
                 <input type="hidden" name="method" value="DELETE">
                 <input type="hidden" name="id" value="${rate.id}">
                 <input type="hidden" name="productId" value="${rate.productId}">
                 <input type="submit" value='<lan:print message="rate.byid.jsp.button.delete"/>'>
        </form>
        </c:if>
        <c:if test = "${rate.unusable == 'true'}">
            <p><lan:print message="rate.byid.jsp.rate_info"/>:
            <lan:print message="rate.byid.jsp.label.name"/>: ${rate.name};
            <lan:print message="rate.byid.jsp.label.price"/>: ${rate.price};
            <lan:print message="rate.byid.jsp.unusable"/>: ${rate.unusable}
            </p>
        </c:if>
        <form method="GET" action="/telecom/operator/home.jsp">
                    <input type="submit" value='<lan:print message="rate.unusable.jsp.button.home"/>'>
                </form>
    </body>
</html>