package com.aurionpro.lending.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.lending.dto.CustomerResponseDTO;
import com.aurionpro.lending.entity.Customer;
import com.aurionpro.lending.entity.LoanOfficer;
import com.aurionpro.lending.entity.User;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.CustomerRepository;
import com.aurionpro.lending.repository.LoanOfficerRepository;
import com.aurionpro.lending.repository.UserRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private LoanOfficerRepository loanOfficerRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public CustomerResponseDTO getCustomerById(int id) {
		logger.debug("Fetching customer with ID: {}", id);
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
		return toResponseDTO(customer);
	}

	@Override
	public List<CustomerResponseDTO> getCustomersByLoanOfficerId(int loanOfficerId) {
		logger.debug("Fetching customers for loan officer ID: {}", loanOfficerId);
		List<Customer> customers = customerRepository.findByLoanOfficerIdAndIsDeletedFalse(loanOfficerId);
		if (customers.isEmpty()) {
			throw new ResourceNotFoundException("No customers found for Loan Officer ID: " + loanOfficerId);
		}
		return customers.stream().map(this::toResponseDTO).collect(Collectors.toList());
	}

	@Override
	public List<CustomerResponseDTO> getAllCustomers(boolean includeDeleted) {
		logger.debug("Fetching all customers, includeDeleted: {}", includeDeleted);
		List<Customer> customers = customerRepository.findAllCustomers(includeDeleted);
		return customers.stream().map(this::toResponseDTO).collect(Collectors.toList());
	}

//	@Override
//	@Transactional
//	public void assignLoanOfficer(int customerId, int loanOfficerId) {
//		logger.debug("Assigning loan officer ID: {} to customer ID: {}", loanOfficerId, customerId);
//		Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
//				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
//
//		LoanOfficer loanOfficer = loanOfficerRepository.findById(loanOfficerId)
//				.orElseThrow(() -> new ResourceNotFoundException("Loan Officer not found with ID: " + loanOfficerId));
//
//		customer.setLoanOfficer(loanOfficer);
//		customerRepository.save(customer);
//	}

	@Override
	@Transactional
	public void assignLoanOfficer(int customerId) {
		logger.debug("Assigning loan officer randomly to customer ID: {}", customerId);

		Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

		List<LoanOfficer> loanOfficers = loanOfficerRepository.findAll();

		if (loanOfficers.isEmpty()) {
			throw new IllegalStateException("No loan officers available for assignment.");
		}

		// Shuffle and cycle logic
		int hash = Integer.hashCode(customerId);
		Collections.shuffle(loanOfficers); // randomize order
		int index = Math.abs(hash) % loanOfficers.size();
		LoanOfficer assignedLoanOfficer = loanOfficers.get(index);

		customer.setLoanOfficer(assignedLoanOfficer);
		customerRepository.save(customer);

		logger.debug("Assigned Loan Officer ID: {} to Customer ID: {}", assignedLoanOfficer.getId(), customer.getId());
	}

	@Override
	@Transactional
	public void softDeleteCustomer(int id) {
		logger.debug("Soft deleting customer with ID: {}", id);
		Customer customer = customerRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

		// Soft delete customer
		customerRepository.updateIsDeletedById(id, true);

		// Soft delete associated user
		User user = customer.getUser();
		if (user != null) {
			logger.info("Soft deleting user with ID: {} for customer ID: {}", user.getId(), id);
			userRepository.updateIsDeletedById(user.getId(), true);
		} else {
			logger.error("No user associated with customer ID: {}", id);
			throw new IllegalStateException("Customer must have an associated user");
		}
	}

	@Override
	@Transactional
	public void selfDeleteCustomer(int customerId) {
		logger.debug("Self soft deleting customer with ID: {}", customerId);
		Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

		// Soft delete customer
		customerRepository.updateIsDeletedById(customerId, true);

		// Soft delete associated user
		User user = customer.getUser();
		if (user != null) {
			logger.info("Self soft deleting user with ID: {} for customer ID: {}", user.getId(), customerId);
			userRepository.updateIsDeletedById(user.getId(), true);
		} else {
			logger.error("No user associated with customer ID: {}", customerId);
			throw new IllegalStateException("Customer must have an associated user");
		}
	}

	private CustomerResponseDTO toResponseDTO(Customer customer) {
		CustomerResponseDTO dto = new CustomerResponseDTO();
		try {
			dto.setId(customer.getId());
			User user = customer.getUser();
			if (user != null) {
				dto.setUsername(user.getUsername());
				dto.setEmail(user.getEmail());
			} else {
				logger.warn("Customer ID: {} has no associated user", customer.getId());
				dto.setUsername(null);
				dto.setEmail(null);
			}
			dto.setLoanOfficerId(customer.getLoanOfficer() != null ? customer.getLoanOfficer().getId() : 0);
			dto.setDeleted(customer.isDeleted());
			logger.debug("Mapped customer ID: {} to DTO: {}", customer.getId(), dto);
		} catch (Exception e) {
			logger.error("Error mapping Customer to CustomerResponseDTO for customer ID: {}", customer.getId(), e);
			throw new RuntimeException("Failed to map customer to DTO", e);
		}
		return dto;
	}
}