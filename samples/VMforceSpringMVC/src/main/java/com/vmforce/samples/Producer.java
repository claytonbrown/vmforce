package com.vmforce.samples;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.salesforce.persistence.datanucleus.annotation.CustomObject;

/**
 * @author jjoergensen@salesforce.com
 */

@Entity
@CustomObject(enableFeeds=true)
public class Producer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

    private String name;
    
    private String city;
    
    private String region;
    
    private String country;
    
//    @OneToMany(fetch=FetchType.EAGER, mappedBy="producer")
//    private List<Wine> wines;

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
