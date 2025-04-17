package com.aurionpro.lending.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoanOfficerResponseDTO {
	private int id;
	private String username;
	private String email;
	private int adminId;
	private List<Integer> customerIds;
}