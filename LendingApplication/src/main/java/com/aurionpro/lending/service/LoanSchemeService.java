package com.aurionpro.lending.service;

import java.util.List;

import com.aurionpro.lending.dto.LoanSchemeRequestDTO;
import com.aurionpro.lending.dto.LoanSchemeResponseDTO;
import com.aurionpro.lending.dto.LoanSchemeUpdateDTO;
import com.aurionpro.lending.entity.DocumentType;

public interface LoanSchemeService {
	LoanSchemeResponseDTO createLoanScheme(int adminId, LoanSchemeRequestDTO requestDTO);

	LoanSchemeResponseDTO getLoanSchemeById(int id);

	List<LoanSchemeResponseDTO> getLoanSchemesByAdminId(int adminId);

	List<LoanSchemeResponseDTO> getAllLoanSchemes();

	void softDeleteLoanScheme(Integer schemeId, Integer adminId);

	List<LoanSchemeResponseDTO> getAllLoanSchemesForAdmin(boolean includeDeleted);

	List<DocumentType> getRequiredDocumentTypes(int schemeId);

	LoanSchemeResponseDTO updateLoanScheme(Integer schemeId, Integer adminId, LoanSchemeUpdateDTO updateDTO);
}