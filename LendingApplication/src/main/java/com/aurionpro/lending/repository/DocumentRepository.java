package com.aurionpro.lending.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.lending.entity.Document;
import com.aurionpro.lending.entity.DocumentStatus;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
	List<Document> findByCustomerId(Integer customerId);

	List<Document> findByLoanLoanId(Integer loanId);

	List<Document> findByLoanLoanId(int loanId);

	List<Document> findByStatus(DocumentStatus status);

	List<Document> findByLoanLoanIdAndStatus(int loanId, DocumentStatus status);
}