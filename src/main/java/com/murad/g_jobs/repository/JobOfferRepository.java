package com.murad.g_jobs.repository;

import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.JobOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> findByCompany(Company company);
    Page<JobOffer> findByCompany(Company company, Pageable pageable);

}
