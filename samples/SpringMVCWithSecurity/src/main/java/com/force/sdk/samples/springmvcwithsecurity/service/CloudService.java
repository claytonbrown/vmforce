package com.force.sdk.samples.springmvcwithsecurity.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.force.sdk.samples.springmvcwithsecurity.model.MyFirstCloudEntity;

/**
 * @author fhossain@salesforce.com
 */

@Service("cloudService")
@Repository
public class CloudService {

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    public MyFirstCloudEntity createCloudEntity(String name, int number) {
        MyFirstCloudEntity acct = new MyFirstCloudEntity(name, number);
        em.persist(acct);
        return acct;
    }
    
    @Transactional(readOnly = true)
    public MyFirstCloudEntity findById(String id) {
        return em.find(MyFirstCloudEntity.class, id);
    }
}
