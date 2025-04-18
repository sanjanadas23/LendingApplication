package com.aurionpro.lending.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.lending.dto.LoanOfficerRequestDTO;
import com.aurionpro.lending.dto.LoanOfficerResponseDTO;
import com.aurionpro.lending.entity.Admin;
import com.aurionpro.lending.entity.Customer;
import com.aurionpro.lending.entity.LoanOfficer;
import com.aurionpro.lending.entity.Role;
import com.aurionpro.lending.entity.User;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.AdminRepository;
import com.aurionpro.lending.repository.LoanOfficerRepository;
import com.aurionpro.lending.repository.RoleRepository;
import com.aurionpro.lending.repository.UserRepository;

@Service
public class LoanOfficerServiceImpl implements LoanOfficerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private LoanOfficerRepository loanOfficerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public LoanOfficerResponseDTO addLoanOfficer(int adminId, LoanOfficerRequestDTO requestDTO) {
		Optional<Admin> adminOpt = adminRepository.findById(adminId);
		if (adminOpt.isEmpty()) {
			throw new ResourceNotFoundException("Admin not found with ID: " + adminId);
		}
		Admin admin = adminOpt.get();

		Optional<Role> roleOpt = roleRepository.findByRoleName("ROLE_LOAN_OFFICER");
		if (roleOpt.isEmpty()) {
			throw new ResourceNotFoundException("Role not found: LOAN_OFFICER");
		}
		Role role = roleOpt.get();

		User user = new User();
		user.setUsername(requestDTO.getUsername());
		user.setEmail(requestDTO.getEmail());
		user.setPassword((passwordEncoder.encode(requestDTO.getPassword())));
		user.setRole(role);
		user = userRepository.save(user);

		LoanOfficer loanOfficer = new LoanOfficer();
		loanOfficer.setUser(user);
		loanOfficer.setAdmin(admin);
		loanOfficer = loanOfficerRepository.save(loanOfficer);

		LoanOfficerResponseDTO dto = new LoanOfficerResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setAdminId(admin.getId());
		dto.setCustomerIds(Collections.emptyList());
		return dto;
	}

	@Override
	public LoanOfficerResponseDTO getLoanOfficerById(int id) {
		Optional<LoanOfficer> loanOfficerOpt = loanOfficerRepository.findById(id);
		if (loanOfficerOpt.isEmpty()) {
			throw new ResourceNotFoundException("Loan Officer not found with ID: " + id);
		}
		LoanOfficer loanOfficer = loanOfficerOpt.get();
		User user = loanOfficer.getUser();

		LoanOfficerResponseDTO dto = new LoanOfficerResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setAdminId(loanOfficer.getAdmin() != null ? loanOfficer.getAdmin().getId() : 0);
		dto.setCustomerIds(loanOfficer.getCustomers() != null
				? loanOfficer.getCustomers().stream().map(Customer::getId).collect(Collectors.toList())
				: Collections.emptyList());
		return dto;
	}

	@Override
	public List<LoanOfficerResponseDTO> getLoanOfficersByAdminId(int adminId) {
		Optional<Admin> adminOpt = adminRepository.findById(adminId);
		if (adminOpt.isEmpty()) {
			throw new ResourceNotFoundException("Admin not found with ID: " + adminId);
		}

		List<LoanOfficer> loanOfficers = loanOfficerRepository.findByAdminId(adminId);
		if (loanOfficers.isEmpty()) {
			throw new ResourceNotFoundException("No loan officers found for Admin ID: " + adminId);
		}
		return loanOfficers.stream().map(loanOfficer -> {
			User user = loanOfficer.getUser();
			LoanOfficerResponseDTO dto = new LoanOfficerResponseDTO();
			dto.setId(user.getId());
			dto.setUsername(user.getUsername());
			dto.setEmail(user.getEmail());
			dto.setAdminId(loanOfficer.getAdmin() != null ? loanOfficer.getAdmin().getId() : 0);
			dto.setCustomerIds(loanOfficer.getCustomers() != null
					? loanOfficer.getCustomers().stream().map(Customer::getId).collect(Collectors.toList())
					: Collections.emptyList());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<LoanOfficerResponseDTO> getAllLoanOfficers() {
		List<LoanOfficer> loanOfficers = loanOfficerRepository.findAll();
		if (loanOfficers.isEmpty()) {
			throw new ResourceNotFoundException("No loan officers found in the system");
		}
		return loanOfficers.stream().map(loanOfficer -> {
			LoanOfficerResponseDTO dto = new LoanOfficerResponseDTO();
			dto.setId(loanOfficer.getId());
			dto.setUsername(loanOfficer.getUser() != null ? loanOfficer.getUser().getUsername() : null);
			dto.setEmail(loanOfficer.getUser() != null ? loanOfficer.getUser().getEmail() : null);
			dto.setAdminId(loanOfficer.getAdmin() != null ? loanOfficer.getAdmin().getId() : 0);
			dto.setCustomerIds(loanOfficer.getCustomers() != null
					? loanOfficer.getCustomers().stream().map(Customer::getId).collect(Collectors.toList())
					: new ArrayList<>());
			return dto;
		}).collect(Collectors.toList());
	}

}