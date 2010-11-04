package com.force.sdk.samples.springmvcwithsecurity.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * @author fhossain@salesforce.com
 */

@Entity
@Table(name = "MyFirstCloudEntity")
public class MyFirstCloudEntity implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String name;
    private int number;

    public MyFirstCloudEntity() {  }

    public MyFirstCloudEntity(String name, int number) {
        this.name = name;
        this.number = number;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
}
