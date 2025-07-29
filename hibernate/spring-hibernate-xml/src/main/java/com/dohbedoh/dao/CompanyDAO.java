package com.dohbedoh.dao;

import com.dohbedoh.model.Company;

import java.util.List;

/**
 * Created by Allan on 12/10/2015.
 */
public interface CompanyDAO {

    /**
     * Delete a company.
     * @param company {@link Company}
     */
    void delete(Company company);

    /**
     * Delete a company by Id.
     * @param companyId a company id
     */
    void deleteById(String companyId);

    /**
     * Find a Company by name.
     * @param name a company name
     * @return {@link Company}
     */
    Company findByName(String name);

    /**
     * Get a the list of all companies.
     * @return {@link List} of {@link Company}
     */
    List<Company> findAll();

    /**
     * Insert a company.
     * @param company a {@link Company}
     */
    void insert(Company company);

    /**
     * Update a company.
     * @param company a {@link Company}
     */
    void update(Company company);
}
