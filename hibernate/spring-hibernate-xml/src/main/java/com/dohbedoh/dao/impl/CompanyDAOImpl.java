package com.dohbedoh.dao.impl;

import com.dohbedoh.dao.AbstractDAO;
import com.dohbedoh.dao.CompanyDAO;
import com.dohbedoh.model.Company;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Allan on 23/10/2015.
 */
public class CompanyDAOImpl extends AbstractDAO implements CompanyDAO {

    public void setSessionFactory(SessionFactory sessionFactory) {super.setSessionFactory(sessionFactory); }

    @Override
    @Transactional
    public void delete(Company company){
        getSession().delete(company);
    }

    @Override
    @Transactional
    public void deleteById(String companyId) {
        Query query = getSession().createSQLQuery("delete from company where company_id = :company_id");
        query.setString("company_id", companyId);
        query.executeUpdate();
    }

    @Override
    @Transactional
    public Company findByName(String name){
        Criteria criteria = getSession().createCriteria(Company.class);
        criteria.add(Restrictions.eq("name", name));
        return (Company) criteria.uniqueResult();
    }

    @Override
    @Transactional
    public List<Company> findAll() {
//        return (List<Company>) getSession()
//                .createCriteria(Company.class)
//                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        return (List<Company>) getSession().createCriteria(Company.class).list();
    }

    @Override
    @Transactional
    public void insert(Company company){
        getSession().save(company);
    }

    @Override
    @Transactional
    public void update(Company company){
        getSession().update(company);
    }
}
