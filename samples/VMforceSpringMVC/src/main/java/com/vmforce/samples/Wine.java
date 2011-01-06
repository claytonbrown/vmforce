package com.vmforce.samples;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.force.sdk.jpa.annotation.CustomObject;

/**
 * @author jjoergensen@salesforce.com
 */

@Entity
@CustomObject(enableFeeds=true)
public class Wine {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
    
    private int vintage;
    
    private String name;
    
    public enum Variety { Cabernet_Sauvignon, Syran, Pinot_Noir, Zinfandel }

    @Enumerated
    private Variety variety;
    
	@ManyToOne
    private Producer producer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getVintage() {
		return vintage;
	}

	public void setVintage(int vintage) {
		this.vintage = vintage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Variety getVariety() {
		return variety;
	}

	public void setVariety(Variety variety) {
		this.variety = variety;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}
    
}
