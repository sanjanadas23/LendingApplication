package com.aurionpro.lending.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.aurionpro.lending.entity.Customer;
import com.aurionpro.lending.repository.CustomerRepository;

@Service
public class CustomerSecurityService {

	@Autowired
	private CustomerRepository customerRepository;

	public boolean isAssignedLoanOfficer(Authentication authentication, Integer customerId) {
		if (!authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_LOAN_OFFICER"))) {
			return false;
		}

		Integer userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
		Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId).orElse(null);

		return customer != null && customer.getLoanOfficer() != null
				&& customer.getLoanOfficer().getUser().getId() == userId;
	}
}