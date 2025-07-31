package com.violetbeach.hibernateplayground.support;

import jakarta.persistence.EntityManager;

public class QueryCountUtil {

    public static long getEntityLoadCount(EntityManager entityManager) {
        return entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics()
            .getEntityLoadCount();
    }

    public static long getSelectQueryCount(EntityManager entityManager) {
        return entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics()
            .getQueryExecutionCount();
    }

    public static void clearAllCount(EntityManager entityManager) {
        entityManager.unwrap(org.hibernate.Session.class).getSessionFactory().getStatistics()
            .clear();
    }

}