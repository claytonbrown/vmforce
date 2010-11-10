<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
	<title>Welcome</title>
</head>
<body>
<h1>
Sign people up for HMB!
</h1>
<form:form commandName="attendee">
<table>
	<tr>
		<td>First Name:</td><td>${attendee.relatedContact.firstName}</td>
	</tr>
	<tr>
		<td>Last Name:</td><td>${attendee.relatedContact.lastName}</td>
	</tr>
	<tr>
		<td>Arrival Date:</td><td><form:input size="40" path="arrivalDate"/></td>
	</tr>
	<tr>
		<td>Departure Date:</td><td><form:input size="40" path="departureDate"/></td>
	</tr>
	<tr>
		<td>Meal Preference:</td><td><form:select path="mealPreference" items="${mealOptions}"/></td>
	</tr>
	<tr>
		<td>Staying at hotel:</td><td><form:checkbox path="stayingAtHotel"/></td>
	</tr>
</table>
<input type="submit" value="Create"/>
</form:form>

</body>
</html>
