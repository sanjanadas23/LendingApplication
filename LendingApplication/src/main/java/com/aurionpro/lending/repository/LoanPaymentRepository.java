package com.aurionpro.lending.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.lending.entity.LoanPayment;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, Integer> {
	List<LoanPayment> findByLoanLoanId(Integer loanId);
}