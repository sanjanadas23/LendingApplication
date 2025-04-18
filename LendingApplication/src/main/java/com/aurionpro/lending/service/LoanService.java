package com.aurionpro.lending.service;

import java.util.List;

import com.aurionpro.lending.dto.LoanRequestDTO;
import com.aurionpro.lending.dto.LoanResponseDTO;
import com.aurionpro.lending.dto.LoanUpdateDTO;

public interface LoanService {
	LoanResponseDTO applyForLoan(LoanRequestDTO requestDTO);

	LoanResponseDTO updateLoanStatus(int loanId, LoanUpdateDTO updateDTO);

	LoanResponseDTO getLoanById(int id);

	List<LoanResponseDTO> getLoansByCustomerId(int customerId);

	List<LoanResponseDTO> getLoansByLoanOfficerId(int loanOfficerId);

	LoanResponseDTO markLoanAsNpa(int loanId, boolean isNpa);
}