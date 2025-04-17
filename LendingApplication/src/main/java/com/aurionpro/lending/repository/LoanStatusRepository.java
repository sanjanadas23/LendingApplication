package com.aurionpro.lending.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.lending.entity.LoanStatus;

public interface LoanStatusRepository extends JpaRepository<LoanStatus, Integer> {
	Optional<LoanStatus> findByStatusName(String statusName);
}