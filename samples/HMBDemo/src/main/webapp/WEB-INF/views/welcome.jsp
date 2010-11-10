<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Welcome</title>
</head>
<body>
<h1>
	Congratulations! You're running Spring on VMforce!
</h1>
<p>
You are connected to Force.com with the following user:
</p>
<table>
<tr>
<td>Username:</td><td>${userInfo.userName}</td>
</tr><tr>
<td>Organization ID:</td><td>${userInfo.organizationId}</td>
</tr>
</table>

<p>Your database contains some sample contacts (showing max 10 contacts):</p>
<table>
<tr>
<th>First Name</th><th>Last Name</th></tr>
<c:forEach items="${contacts}" var="contact">
	<tr>
		<td>${contact.firstName}</td>
		<td>${contact.lastName}</td>
		<td><a href="${contact.id}/create">Add to HMB attendees</a></td>
	</tr>
</c:forEach>
</table>

</body>
</html>
