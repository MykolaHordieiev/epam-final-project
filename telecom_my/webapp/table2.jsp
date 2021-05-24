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
  font-size: 0.8em;
  padding: 5px 7px;
  border: 1px solid #A9E2CC;
}
</style>
<table>
  <caption>Table of Products with rate</caption>
  <tr>
    <th colspan="2" class="first">Info about product</th>
    <th colspan="3">Info about rate of product</th>
  </tr>
  <tr>
    <td class="first">id</td>
    <td class="first">name</td>
    <td class="first">id</td>
    <td class="first">name</td>
    <td class="first">price</td>
  </tr>
  <tr>
    <td rowspan="3" class="first">1</td>
    <td rowspan="3" class="first">Mobile</td>
    <td>1</td>
    <td>without Internet</td>
    <td>10</td>
  </tr>
  <tr>
    <td>2</td>
    <td>with Internet</td>
    <td>20</td>
  </tr>
  <tr>
    <td>3</td>
    <td>Super Mobile</td>
    <td>30</td>
    </tr>
  <tr>
    <td rowspan="3" class="first">2</td>
    <td rowspan="3" class="first">Internet</td>
    <td>1</td>
    <td>low Internet</td>
    <td>20</td>
    </tr>
  <tr>
    <td>2</td>
    <td>medium Internet</td>
    <td>40</td>
    </tr>
  <tr>
    <td>3</td>
    <td>High Internet</td>
    <td>50</td>
    </tr>
  <tr>
    <td rowspan="3" class="first">3</td>
    <td rowspan="3" class="first">TV</td>
    <td>1</td>
    <td>100+programs</td>
    <td>25</td>
    </tr>
  <tr>
    <td>2</td>
    <td>500+programs</td>
    <td>40</td>
    </tr>
  <tr>
    <td>3</td>
    <td>1500+programs</td>
    <td>50</td>
    </tr>
    <tr>
        <td rowspan="3" class="first">4</td>
        <td rowspan="3" class="first">Streaming</td>
        <td>1</td>
        <td>first</td>
        <td>25</td>
        </tr>
      <tr>
        <td>2</td>
        <td>second</td>
        <td>45</td>
        </tr>
      <tr>
        <td>3</td>
        <td>third</td>
        <td>60</td>
        </tr>
  </table>

         <select name="entity">
             <c:forEach items="${entity}" var="entity">
                 <option value="${entity.key}">${entity.value}</option>
             </c:forEach>
         </select>



          <form action="test" method="GET">
                         <select name="Locale">
                             <c:forEach var="Locale" items="${sessionScope.LOCALE}">
                                 <option value="${Locale}">
                                     ${Locale.toUpperCase()}
                                 </option>
                             </c:forEach>
                         </select>
                         <input type="submit" value="go">
                     </form>