package com.murad.g_jobs.repository;

import com.murad.g_jobs.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
