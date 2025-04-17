package com.aurionpro.lending.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LoanSchemeResponseDTO {
	private int id;
	private String schemeName;
	private BigDecimal interestRate;
	private Integer tenureMonths;
	private int adminId;
	private List<String> requiredDocumentTypeNames;
	private boolean isDeleted;
}