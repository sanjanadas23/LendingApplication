package com.aurionpro.lending.dto;

import lombok.Data;

@Data
public class DocumentResponseDTO {
	private int documentId;
	private String documentName;
	private String documentUrl;
	private int customerId;
	private String documentTypeName;
	private String status;
}