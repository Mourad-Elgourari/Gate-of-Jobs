package com.murad.g_jobs.repository;

import com.murad.g_jobs.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
}
