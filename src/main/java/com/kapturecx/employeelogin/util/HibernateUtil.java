package com.kapturecx.employeelogin.util;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    @Autowired
    HibernateUtil(SessionFactory sessionFactory) {
        HibernateUtil.sessionFactory = sessionFactory;
    }
    public static <T> boolean saveOrUpdate(T classObj) {
        boolean success = false;
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(classObj);
            tx.commit();
            success = true;
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return success;
    }
    private static <T> List<T> runQueryHelper(String queryString, Map<String, Object> parametersToSet,
                                              Class<T> className, Integer limit, Integer offset) {
        List<T> list = null;
        try (Session session = sessionFactory.openSession()) {
            TypedQuery<T> query = session.createQuery(queryString, className);

            if (parametersToSet != null && !parametersToSet.isEmpty()) {
                parametersToSet.forEach(query::setParameter);
            }

            if (limit != null && offset != null) {
                query.setMaxResults(limit);
                query.setFirstResult(offset);
            }

            list = query.getResultList(); // this never returns null;
            if (list != null && list.size() == 0) {
                list = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static <T> List<T> runQuery(String queryString, Map<String, Object> parametersToSet, Class<T> className, Integer limit, Integer offset) {
        return runQueryHelper(queryString, parametersToSet, className, limit, offset);
    }
    public static <T> T getSingleResult(String queryString, Map<String, Object> parametersToSet, Class<T> className) {
        return getSingleResult(runQuery(queryString, parametersToSet, className, 1, 0));
    }
    public static <T> T getSingleResult(List<T> list) {
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}



