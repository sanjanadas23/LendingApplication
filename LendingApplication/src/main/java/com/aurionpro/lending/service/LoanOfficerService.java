package com.aurionpro.lending.service;

import java.util.List;

import com.aurionpro.lending.dto.LoanOfficerRequestDTO;
import com.aurionpro.lending.dto.LoanOfficerResponseDTO;

public interface LoanOfficerService {
	LoanOfficerResponseDTO addLoanOfficer(int adminId, LoanOfficerRequestDTO requestDTO);

	LoanOfficerResponseDTO getLoanOfficerById(int id);

	List<LoanOfficerResponseDTO> getLoanOfficersByAdminId(int adminId);

	List<LoanOfficerResponseDTO> getAllLoanOfficers();
}