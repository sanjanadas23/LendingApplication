package com.aurionpro.lending.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.lending.entity.Customer;
import com.aurionpro.lending.entity.Loan;
import com.aurionpro.lending.entity.LoanOfficer;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.CustomerRepository;
import com.aurionpro.lending.repository.LoanOfficerRepository;
import com.aurionpro.lending.repository.LoanRepository;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private LoanOfficerRepository loanOfficerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public Map<String, Object> generateLoanOfficerReport(int loanOfficerId) {
		LoanOfficer officer = loanOfficerRepository.findById(loanOfficerId)
				.orElseThrow(() -> new ResourceNotFoundException("Loan Officer not found with ID: " + loanOfficerId));
		List<Loan> loans = loanRepository.findByLoanOfficerId(loanOfficerId);
		List<Loan> activeLoans = loans.stream().filter(l -> !l.getStatus().getStatusName().equals("PAID_OFF")
				&& !l.getStatus().getStatusName().equals("CLOSED")).collect(Collectors.toList());

		Map<String, Object> report = new HashMap<>();
		report.put("loansOffered", loans.size());
		report.put("rejected", loans.stream().filter(l -> l.getStatus().getStatusName().equals("REJECTED")).count());
		report.put("inProcess", loans.stream().filter(l -> l.getStatus().getStatusName().equals("PENDING")
				|| l.getStatus().getStatusName().equals("UNDER_REVIEW")).count());
		report.put("amountDisbursed",
				loans.stream()
						.filter(l -> l.getStatus().getStatusName().equals("APPROVED")
								|| (l.getNpaStatus() != null && l.getNpaStatus().getStatusName().equals("NPA")))
						.map(Loan::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
		report.put("customersEntertained", customerRepository.findByLoanOfficerIdAndIsDeletedFalse(loanOfficerId)
				.stream().map(Customer::getId).distinct().count());
		report.put("npas", activeLoans.stream()
				.filter(l -> l.getNpaStatus() != null && l.getNpaStatus().getStatusName().equals("NPA")).count());
		report.put("redFlaggedCustomers", customerRepository.findByLoanOfficerIdAndIsDeletedFalse(loanOfficerId)
				.stream().filter(Customer::isRedFlagged).count());

		return report;
	}
}