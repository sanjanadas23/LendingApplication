package com.aurionpro.lending.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Document {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int documentId;

	@NotBlank(message = "Document name is required")
	@Column(nullable = false)
	private String documentName;

	@NotBlank(message = "Document URL is required")
	@Column(nullable = false)
	private String documentUrl;

	@NotBlank(message = "Public ID is required")
	@Column(nullable = false)
	private String publicId;

	@NotNull(message = "Customer is required")
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "loan_id", nullable = true)
	private Loan loan;

	@NotNull(message = "Document type is required")
	@ManyToOne
	@JoinColumn(name = "document_type_id", nullable = false)
	private DocumentType documentType;

	@NotNull(message = "Status is required")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentStatus status = DocumentStatus.PENDING_VERIFICATION;

}