<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!
</h1>
List of wines:
<ul>
<c:forEach items="${wines}" var="wine">
	<li>${wine.name} (by producer ${wine.producer.name})</li>
</c:forEach>
</ul>
<form action="" method="POST">
	<input type="submit" name="New Wine" value="New Wine"/>
</form>
</body>
</html>
