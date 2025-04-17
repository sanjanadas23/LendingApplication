package com.aurionpro.lending.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminResponseDTO {
	private int id;
	private String username;
	private String email;
	private List<Integer> loanOfficerIds;
	private List<Integer> loanSchemeIds;
}