package com.vmforce.hmbdemo;

import java.net.URL;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.salesforce.persistence.datanucleus.annotation.CustomField;
import com.sforce.soap.metadata.FieldType;

@Entity
public class HMBAttendee {

	public enum MealPrefEnum { Standard, Vegetarian, Vegan, Kosher, Muslim };

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	String id;
	
	String name;
	
	
	Date arrivalDate;

	Date departureDate;
	
	boolean stayingAtHotel;

	@Enumerated(EnumType.STRING)
	MealPrefEnum mealPreference;
	
	@CustomField(type=FieldType.Phone)
	String emergencyPhone;
	
	@CustomField(type=FieldType.Email)
	String mobileEmail;

	@Basic
	URL chatterHomePage;
	
	@Column(name= "Contact")
	@ManyToOne
	Contact relatedContact;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public boolean isStayingAtHotel() {
		return stayingAtHotel;
	}

	public void setStayingAtHotel(boolean stayingAtHotel) {
		this.stayingAtHotel = stayingAtHotel;
	}

	public MealPrefEnum getMealPreference() {
		return mealPreference;
	}

	public void setMealPreference(MealPrefEnum mealPreference) {
		this.mealPreference = mealPreference;
	}

	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	public String getMobileEmail() {
		return mobileEmail;
	}

	public void setMobileEmail(String mobileEmail) {
		this.mobileEmail = mobileEmail;
	}

	public URL getChatterHomePage() {
		return chatterHomePage;
	}

	public void setChatterHomePage(URL chatterHomePage) {
		this.chatterHomePage = chatterHomePage;
	}

	public Contact getRelatedContact() {
		return relatedContact;
	}

	public void setRelatedContact(Contact relatedContact) {
		this.relatedContact = relatedContact;
	}

	
}
