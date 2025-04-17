package com.aurionpro.lending.dto;

import lombok.Data;

@Data
public class CustomerResponseDTO {
	private int id;
	private String username;
	private String email;
	private int loanOfficerId;
	private boolean isDeleted;
}