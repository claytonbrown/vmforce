package com.vmforce.samples;


import static org.junit.Assert.*;
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

public class QueryTest {
	
	private EntityManager em;
	
	private String entityName;
	private String entityId;

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory fac = Persistence.createEntityManagerFactory("forceDatabase");
		em = fac.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Wine entity = new Wine();
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
			Wine entity = em.find(Wine.class, entityId);

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
		List<Wine> l = em.createQuery("SELECT m FROM Wine m WHERE name = :name").setParameter("name",entityName).getResultList();
		assertTrue(l.size()==1);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void updateWine() {

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			
			// With Hibernate, you can do this:
			// Wine w = new Wine();
			// w.setId(entityId);
			// w.setName(entityName);
			// w.setVintage(1979);
			// Wine attached = em.merge(w);

			// With DataNucleus, we do this instead:
			
			Wine w = em.find(Wine.class, entityId);
			w.setVintage(1979);
			
			tx.commit();
			
			//assertTrue(attached.getId().equals(entityId));
			
			List<Wine> l = em.createQuery("SELECT m FROM Wine m WHERE name = :name").setParameter("name",entityName).getResultList();
			
			assertEquals(1,l.size());
			
			assertEquals(1979, l.get(0).getVintage());
			
		}
		finally {
			if(tx.isActive()) {
				tx.rollback();
			}
		}
		
	}

}
