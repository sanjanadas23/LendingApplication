package com.aurionpro.lending.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.lending.dto.JwtResponseDTO;
import com.aurionpro.lending.dto.LoginRequestDTO;
import com.aurionpro.lending.dto.UserRequestDTO;
import com.aurionpro.lending.dto.UserResponseDTO;
import com.aurionpro.lending.entity.Customer;
import com.aurionpro.lending.entity.Role;
import com.aurionpro.lending.entity.User;
import com.aurionpro.lending.exception.BusinessRuleViolationException;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.AdminRepository;
import com.aurionpro.lending.repository.CustomerRepository;
import com.aurionpro.lending.repository.LoanOfficerRepository;
import com.aurionpro.lending.repository.RoleRepository;
import com.aurionpro.lending.repository.UserRepository;
import com.aurionpro.lending.security.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private LoanOfficerRepository loanOfficerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomerService customerService;

	@Override
	public UserResponseDTO registerUser(UserRequestDTO userRequestDTO, String roleName) {
		// Validate role name is only CUSTOMER
		if (!"CUSTOMER".equalsIgnoreCase(roleName)) {
			throw new BusinessRuleViolationException("Only customers can register using this method.");
		}

		String prefixedRoleName = "ROLE_CUSTOMER";
		Role role = roleRepository.findByRoleName(prefixedRoleName)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + prefixedRoleName));

		if (userRepository.findByEmailAndIsDeletedFalse(userRequestDTO.getEmail()).isPresent()) {
			throw new BusinessRuleViolationException("Email already registered: " + userRequestDTO.getEmail());
		}

		if (userRepository.findByUsernameAndIsDeletedFalse(userRequestDTO.getUsername()).isPresent()) {
			throw new BusinessRuleViolationException("Username already taken: " + userRequestDTO.getUsername());
		}

		User user = new User();
		user.setUsername(userRequestDTO.getUsername());
		user.setEmail(userRequestDTO.getEmail());
		user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
		user.setRole(role);

		user = userRepository.save(user);

		Customer customer = new Customer();
		customer.setUser(user);
		customer = customerRepository.save(customer);

		// Auto assign loan officer
		customerService.assignLoanOfficer(customer.getId());

		return toResponseDTO(user);
	}

	@Override
	public UserResponseDTO getUserById(int id) {
		User user = userRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
		return toResponseDTO(user);
	}

	@Override
	public JwtResponseDTO login(LoginRequestDTO loginRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = userRepository.findByUsernameAndIsDeletedFalse(loginRequestDTO.getUsername())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().getRoleName());

		JwtResponseDTO response = new JwtResponseDTO();
		response.setToken(token);
		response.setUserId(user.getId());
		response.setUsername(user.getUsername());
		response.setRole(user.getRole().getRoleName());

		String roleName = user.getRole().getRoleName();
		if ("ROLE_ADMIN".equals(roleName)) {
			adminRepository.findByUserId(user.getId()).ifPresent(admin -> response.setAdminId(admin.getId()));
		} else if ("ROLE_CUSTOMER".equals(roleName)) {
			customerRepository.findByUserId(user.getId())
					.ifPresent(customer -> response.setCustomerId(customer.getId()));
		} else if ("ROLE_LOAN_OFFICER".equals(roleName)) {
			loanOfficerRepository.findByUserId(user.getId())
					.ifPresent(officer -> response.setLoanOfficerId(officer.getId()));
		}

		return response;
	}

	@Override
	public List<UserResponseDTO> getAllUsers(boolean includeDeleted) {
		List<User> users = userRepository.findAllUsers(includeDeleted);
		return users.stream().map(this::toResponseDTO).collect(Collectors.toList());
	}

	private UserResponseDTO toResponseDTO(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
		dto.setDeleted(user.isDeleted());
		return dto;
	}

}