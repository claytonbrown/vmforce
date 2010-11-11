package com.force.sdk.samples.springmvcwithsecurity.test;


import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.force.sdk.samples.springmvcwithsecurity.model.MyFirstCloudEntity;

public class QueryTest {
	
	private EntityManager em;
	
	private String entityName;
	private String entityId;

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory fac = Persistence.createEntityManagerFactory("cloudDatabase");
		em = fac.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			MyFirstCloudEntity entity = new MyFirstCloudEntity();
			Random r = new Random();
			entityName = Long.toString(Math.abs(r.nextLong()), 36);
			entity.setName(entityName);
			
			em.persist(entity);
	
			em.flush();
			tx.commit();
			entityId = entity.getId();
			
		}
		finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}
	}
	
	@After
	public void tearDown() throws Exception {

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			MyFirstCloudEntity entity = em.find(MyFirstCloudEntity.class, entityId);

			em.remove(entity);
			
			em.flush();
			
			tx.commit();
		}
		finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Test
	public void getAllPlansForOneAccount() {
		List<MyFirstCloudEntity> l = em.createQuery("SELECT m FROM MyFirstCloudEntity m WHERE name = :name").setParameter("name",entityName).getResultList();
		assertTrue(l.size()==1);
	}
}
