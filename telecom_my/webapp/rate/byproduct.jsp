<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>rate by id</title>
        <meta charset="UTF-8">
    </head>
    <body>

<p><button onclick="sortName(false)">Sort name</button></p>
<p><button onclick="sortName(true)">Sort name inverse</button></p>
<p><button onclick="sortPrice(false)">Sort prise</button></p>
<p><button onclick="sortPrice(true)">Sort prise inverse</button></p>

<table id="rates">
  <tr>
    <th>Name</th>
    <th>Prise</th>
  </tr>
  <c:forEach items="${rates}" var="rate">
  <tr>
    <td>${rate.name}</td>
    <td>${rate.price}</td>
    <c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">
        <td>
            <form method="GET" action="/telecom/service/rate/info">
                <input type="hidden" name="id" value="${rate.id}"/>
                <input type="submit" value="rate info">
            </form>
         </td>
    </c:if>
    <c:if test = "${sessionScope.user.userRole == 'SUBSCRIBER'}">
        <c:if test = "${subscriber.lock == 'false'}">
            <td>
                <form method="POST" action="/telecom/service/add/subscribing">
                    <input type="hidden" name="rateId" value="${rate.id}"/>
                    <input type="hidden" name="productId" value="${productId}"/>
                    <input type="submit" value="subscribing">
                </form>
             </td>
        </c:if>
    </c:if>
  </tr>
  </c:forEach>
</table>
<c:if test = "${sessionScope.user.userRole == 'OPERATOR'}">

      <form method="GET" action="/telecom/service/rate/add">
            <input type="hidden" name="id" value="${productId}"/>
                <input type="submit" value="add new rate">
      </form>

    </c:if>

     <form method="GET" action="/telecom/download/rate">
                <input type="hidden" name="productId" value="${productId}"/>
                <input type="submit" value="download">
                <select name="format">
                    <option value=".txt">.txt</option>
                    <option value=".pdf">.pdf</option>
                    <option value=".docx">.docx</option>
                </select>
          </form>



<script>
function sortName(inverse) {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById("rates");
  switching = true;
  /*Make a loop that will continue until
  no switching has been done:*/
  while (switching) {
    //start by saying: no switching is done:
    switching = false;
    rows = table.rows;
    /*Loop through all table rows (except the
    first, which contains table headers):*/
    for (i = 1; i < (rows.length - 1); i++) {
      //start by saying there should be no switching:
      shouldSwitch = false;
      /*Get the two elements you want to compare,
      one from current row and one from the next:*/
      x = rows[i].getElementsByTagName('TD')[0];
      y = rows[i + 1].getElementsByTagName('TD')[0];
      //check if the two rows should switch place:
      if ((x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) ^ inverse) {
        //if so, mark as a switch and break the loop:
        shouldSwitch = true;
        break;
      }
    }
    if (shouldSwitch) {
      /*If a switch has been marked, make the switch
      and mark that a switch has been done:*/
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
  }
}

function sortPrice(inverse) {
  var table, rows, switching, i, x, y, shouldSwitch;
  table = document.getElementById("rates");
  switching = true;
  /*Make a loop that will continue until
  no switching has been done:*/
  while (switching) {
    //start by saying: no switching is done:
    switching = false;
    rows = table.rows;
    /*Loop through all table rows (except the
    first, which contains table headers):*/
    for (i = 1; i < (rows.length - 1); i++) {
      //start by saying there should be no switching:
      shouldSwitch = false;
      /*Get the two elements you want to compare,
      one from current row and one from the next:*/
      x = rows[i].getElementsByTagName('TD')[1];
      y = rows[i + 1].getElementsByTagName('TD')[1];
      //check if the two rows should switch place:
      if ((parseInt(x.innerHTML, 10) > parseInt(y.innerHTML, 10)) ^ inverse) {
        //if so, mark as a switch and break the loop:
        shouldSwitch = true;
        break;
      }
    }
    if (shouldSwitch) {
      /*If a switch has been marked, make the switch
      and mark that a switch has been done:*/
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
    }
  }
}
        </script>
    </body>
</html>