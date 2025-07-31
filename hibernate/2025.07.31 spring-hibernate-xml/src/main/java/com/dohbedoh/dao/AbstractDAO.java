package com.dohbedoh.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by Allan on 23/10/2015.
 */
public abstract class AbstractDAO {

    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected void setSessionFactory(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory; }

    public void persist(Object entity) {
        getSession().persist(entity);
    }

    public void delete(Object entity) {
        getSession().delete(entity);
    }
}