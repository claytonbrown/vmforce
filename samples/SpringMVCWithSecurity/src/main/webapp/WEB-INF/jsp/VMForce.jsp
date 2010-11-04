<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
Account info:<c:out value="${model.account.id}"/>, <c:out value="${model.account.name}"/>, <c:out value="${model.account.number}"/>
<br/><br/>

  <form action="/template/" method="get">
    <input type="submit" name="back" value="Back" title="Back"/>
  </form>

</body>
</html>
