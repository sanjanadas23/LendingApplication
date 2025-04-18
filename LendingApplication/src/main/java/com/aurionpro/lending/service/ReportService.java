package com.aurionpro.lending.service;

import java.util.Map;

public interface ReportService {
	Map<String, Object> generateLoanOfficerReport(int loanOfficerId);
}