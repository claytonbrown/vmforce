<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
	<title>Wine Detail - ${wine.name}</title>
</head>
<body>

<form:form commandName="wine">
<table>
	<tr>
		<td>Name:</td><td><form:input size="40" path="name"/></td>
	</tr>
	<tr>
		<td>Vintage:</td><td><form:input size="4" path="vintage"/></td>
	</tr>
	<tr>
		<td>Variety</td><td><form:select path="variety" items="${varietyOptions}"/></td>
	</tr>
	<tr>
		<td>Producer</td><td><form:input size="40" path="producer.name"/></td>
	</tr>
</table>
<input type="submit" value="Save"/>
</form:form>
</body>
</html>
