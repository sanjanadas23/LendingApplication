package com.aurionpro.lending.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.lending.entity.LoanOfficer;

public interface LoanOfficerRepository extends JpaRepository<LoanOfficer, Integer> {
	List<LoanOfficer> findByAdminId(int adminId);

	Optional<LoanOfficer> findByUserId(int userId);
}