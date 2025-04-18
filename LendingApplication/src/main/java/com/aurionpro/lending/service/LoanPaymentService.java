package com.aurionpro.lending.service;

import java.math.BigDecimal;
import java.util.List;

import com.aurionpro.lending.dto.LoanPaymentResponseDTO;
import com.aurionpro.lending.dto.LoanResponseDTO;

public interface LoanPaymentService {
	void createLoanPayments(int loanId);

	List<LoanResponseDTO> getPendingPaymentsByCustomerId(int customerId);

	void processRepayment(int loanPaymentId);

	List<LoanPaymentResponseDTO> getPaymentsByLoanId(int loanId, String status);

	BigDecimal getPaymentAmount(int loanPaymentId);

	LoanPaymentResponseDTO getPaymentDetails(int loanPaymentId);

	void approveNpaStatus(int loanId, boolean approve);

	void checkAndFlagNpaLoans();
}