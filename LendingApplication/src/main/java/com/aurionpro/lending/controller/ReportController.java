package com.aurionpro.lending.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.lending.service.ReportService;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@GetMapping("/loan-officer/{loanOfficerId}")
	public ResponseEntity<Map<String, Object>> generateLoanOfficerReport(@PathVariable int loanOfficerId) {
		Map<String, Object> report = reportService.generateLoanOfficerReport(loanOfficerId);
		return ResponseEntity.ok(report);
	}
}