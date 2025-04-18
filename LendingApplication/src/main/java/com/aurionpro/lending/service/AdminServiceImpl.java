package com.aurionpro.lending.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.lending.dto.AdminResponseDTO;
import com.aurionpro.lending.entity.Admin;
import com.aurionpro.lending.entity.LoanOfficer;
import com.aurionpro.lending.entity.LoanScheme;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public AdminResponseDTO getAdminById(int id) {
		Optional<Admin> adminOpt = adminRepository.findById(id);
		if (adminOpt.isEmpty()) {
			throw new ResourceNotFoundException("Admin not found with ID: " + id);
		}
		Admin admin = adminOpt.get();

		AdminResponseDTO dto = new AdminResponseDTO();
		dto.setId(admin.getId());
		dto.setEmail(admin.getUser() != null ? admin.getUser().getEmail() : null);
		dto.setUsername(admin.getUser() != null ? admin.getUser().getUsername() : null);
		dto.setLoanOfficerIds(admin.getLoanOfficers() != null
				? admin.getLoanOfficers().stream().map(LoanOfficer::getId).collect(Collectors.toList())
				: new ArrayList<>());
		dto.setLoanSchemeIds(admin.getLoanSchemes() != null
				? admin.getLoanSchemes().stream().map(LoanScheme::getId).collect(Collectors.toList())
				: new ArrayList<>());
		return dto;
	}

	@Override
	public List<AdminResponseDTO> getAllAdmins() {
		List<Admin> admins = adminRepository.findAll();
		if (admins.isEmpty()) {
			throw new ResourceNotFoundException("No admins found in the system");
		}
		return admins.stream().map(admin -> {
			AdminResponseDTO dto = new AdminResponseDTO();
			dto.setId(admin.getId());
			dto.setEmail(admin.getUser() != null ? admin.getUser().getEmail() : null);
			dto.setUsername(admin.getUser() != null ? admin.getUser().getUsername() : null);
			dto.setLoanOfficerIds(admin.getLoanOfficers() != null
					? admin.getLoanOfficers().stream().map(LoanOfficer::getId).collect(Collectors.toList())
					: new ArrayList<>());
			dto.setLoanSchemeIds(admin.getLoanSchemes() != null
					? admin.getLoanSchemes().stream().map(LoanScheme::getId).collect(Collectors.toList())
					: new ArrayList<>());
			return dto;
		}).collect(Collectors.toList());
	}
}