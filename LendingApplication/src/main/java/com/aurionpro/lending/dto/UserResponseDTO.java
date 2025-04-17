package com.aurionpro.lending.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
	private int id;
	private String username;
	private String email;
	private String roleName;
	private boolean isDeleted;
}