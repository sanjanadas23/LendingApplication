package com.aurionpro.lending.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.lending.dto.LoanSchemeRequestDTO;
import com.aurionpro.lending.dto.LoanSchemeResponseDTO;
import com.aurionpro.lending.dto.LoanSchemeUpdateDTO;
import com.aurionpro.lending.entity.Admin;
import com.aurionpro.lending.entity.DocumentType;
import com.aurionpro.lending.entity.LoanScheme;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.AdminRepository;
import com.aurionpro.lending.repository.DocumentTypeRepository;
import com.aurionpro.lending.repository.LoanSchemeRepository;

@Service
public class LoanSchemeServiceImpl implements LoanSchemeService {

	@Autowired
	private LoanSchemeRepository loanSchemeRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private DocumentTypeRepository documentTypeRepository;

	@Override
	public LoanSchemeResponseDTO createLoanScheme(int adminId, LoanSchemeRequestDTO requestDTO) {
		Admin admin = adminRepository.findById(adminId)
				.orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));

		List<DocumentType> requiredDocs = requestDTO.getRequiredDocumentTypeIds().stream()
				.map(id -> documentTypeRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Document type not found with ID: " + id)))
				.collect(Collectors.toList());

		LoanScheme loanScheme = new LoanScheme();
		loanScheme.setSchemeName(requestDTO.getSchemeName());
		loanScheme.setInterestRate(requestDTO.getInterestRate());
		loanScheme.setTenureMonths(requestDTO.getTenureMonths());
		loanScheme.setAdmin(admin);
		loanScheme.setRequiredDocumentTypes(requiredDocs);

		loanScheme = loanSchemeRepository.save(loanScheme);

		return toResponseDTO(loanScheme);
	}

	@Override
	public LoanSchemeResponseDTO getLoanSchemeById(int id) {
		LoanScheme loanScheme = loanSchemeRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan scheme not found with ID: " + id));
		return toResponseDTO(loanScheme);
	}

	@Override
	public List<LoanSchemeResponseDTO> getLoanSchemesByAdminId(int adminId) {
		List<LoanScheme> schemes = loanSchemeRepository.findByAdminIdAndIsDeletedFalse(adminId);
		if (schemes.isEmpty()) {
			throw new ResourceNotFoundException("No loan schemes found for Admin ID: " + adminId);
		}
		return schemes.stream().map(this::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	public List<LoanSchemeResponseDTO> getAllLoanSchemes() {
		List<LoanScheme> schemes = loanSchemeRepository.findAllByIsDeletedFalse();
		return schemes.stream().map(this::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void softDeleteLoanScheme(Integer schemeId, Integer adminId) {
		LoanScheme loanScheme = loanSchemeRepository.findByIdAndIsDeletedFalse(schemeId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan scheme not found with ID: " + schemeId));

		if (loanScheme.getAdmin().getId() != adminId) {
			throw new IllegalStateException("Only the admin who created the scheme can soft-delete it");
		}

		loanSchemeRepository.updateIsDeletedById(schemeId, true);
	}

	@Override
	public List<LoanSchemeResponseDTO> getAllLoanSchemesForAdmin(boolean includeDeleted) {
		List<LoanScheme> schemes = loanSchemeRepository.findAllLoanSchemes(includeDeleted);
		return schemes.stream().map(this::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public LoanSchemeResponseDTO updateLoanScheme(Integer schemeId, Integer adminId, LoanSchemeUpdateDTO updateDTO) {
		LoanScheme loanScheme = loanSchemeRepository.findByIdAndIsDeletedFalse(schemeId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan scheme not found with ID: " + schemeId));

		if (loanScheme.getAdmin().getId() != adminId) {
			throw new IllegalStateException("Only the admin who created the scheme can update it");
		}

		List<DocumentType> requiredDocs = updateDTO.getRequiredDocumentTypeIds().stream()
				.map(id -> documentTypeRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Document type not found with ID: " + id)))
				.collect(Collectors.toList());

		loanScheme.setInterestRate(updateDTO.getInterestRate());
		loanScheme.setTenureMonths(updateDTO.getTenureMonths());
		loanScheme.setRequiredDocumentTypes(requiredDocs);

		loanScheme = loanSchemeRepository.save(loanScheme);

		return toResponseDTO(loanScheme);
	}

	@Override
	public List<DocumentType> getRequiredDocumentTypes(int schemeId) {
		LoanScheme loanScheme = loanSchemeRepository.findById(schemeId)
				.orElseThrow(() -> new RuntimeException("Loan scheme not found"));
		return loanScheme.getRequiredDocumentTypes();
	}

	private LoanSchemeResponseDTO toResponseDTO(LoanScheme loanScheme) {
		LoanSchemeResponseDTO dto = new LoanSchemeResponseDTO();
		dto.setId(loanScheme.getId());
		dto.setSchemeName(loanScheme.getSchemeName());
		dto.setInterestRate(loanScheme.getInterestRate());
		dto.setTenureMonths(loanScheme.getTenureMonths());
		dto.setAdminId(loanScheme.getAdmin() != null ? loanScheme.getAdmin().getId() : 0);
		dto.setRequiredDocumentTypeNames(loanScheme.getRequiredDocumentTypes().stream().map(DocumentType::getTypeName)
				.collect(Collectors.toList()));
		dto.setDeleted(loanScheme.isDeleted());
		return dto;
	}
}