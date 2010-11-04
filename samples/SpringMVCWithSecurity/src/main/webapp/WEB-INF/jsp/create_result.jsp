<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
Account info:<c:out value="${model.entity.id}"/>, <c:out value="${model.entity.name}"/>, <c:out value="${model.entity.number}"/>
<br/><br/>

  <form action="." method="get">
    <input type="submit" name="back" value="Back" title="Back"/>
  </form>

</body>
</html>
