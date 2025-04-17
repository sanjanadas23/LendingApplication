package com.aurionpro.lending.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DocumentVerificationDTO {
	@NotBlank(message = "Status is required")
	@Pattern(regexp = "^(APPROVED|REJECTED)$", message = "Status must be APPROVED or REJECTED")
	private String status;
}