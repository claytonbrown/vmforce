<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
	<title>HMB Attendee</title>
</head>
<body>
<p>
The following contact was added as HMB attendee:
</p>
<table>
	<tr>
		<td>Database.com ID:</td><td>${attendee.id}</td>
	</tr>
	<tr>
		<td>First Name:</td><td>${attendee.relatedContact.firstName}</td>
	</tr>
	<tr>
		<td>Last Name:</td><td>${attendee.relatedContact.lastName}</td>
	</tr>
	<tr>
		<td>Arrival Date:</td><td>${attendee.arrivalDate}</td>
	</tr>
	<tr>
		<td>Departure Date:</td><td>${attendee.departureDate}</td>
	</tr>
	<tr>
		<td>Meal Preference:</td><td>${attendee.mealPreference}</td>
	</tr>
	<tr>
		<td>Staying at hotel:</td><td>${attendee.stayingAtHotel}</td>
	</tr>
</table>

</body>
</html>
