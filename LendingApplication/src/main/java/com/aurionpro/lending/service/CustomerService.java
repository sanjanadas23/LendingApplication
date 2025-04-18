package com.aurionpro.lending.service;

import java.util.List;

import com.aurionpro.lending.dto.CustomerResponseDTO;

public interface CustomerService {
	CustomerResponseDTO getCustomerById(int id);

	List<CustomerResponseDTO> getCustomersByLoanOfficerId(int loanOfficerId);

	List<CustomerResponseDTO> getAllCustomers(boolean includeDeleted);

	void assignLoanOfficer(int customerId);

	void softDeleteCustomer(int id);

	void selfDeleteCustomer(int customerId);
}