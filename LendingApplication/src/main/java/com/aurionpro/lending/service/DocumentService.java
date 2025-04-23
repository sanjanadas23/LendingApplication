package com.aurionpro.lending.service;

//import java.util.List;
//
//import com.aurionpro.lending.dto.DocumentRequestDTO;
//import com.aurionpro.lending.dto.DocumentResponseDTO;
//import com.aurionpro.lending.dto.DocumentVerificationDTO;
//
//
//public interface DocumentService {
//	DocumentResponseDTO uploadDocument(DocumentRequestDTO requestDTO);
//
//	DocumentResponseDTO getDocumentById(int id);
//
//	List<DocumentResponseDTO> getDocumentsByCustomerId(int customerId);
//
//	List<DocumentResponseDTO> getDocumentsByLoanId(int loanId);
//
//	DocumentResponseDTO verifyDocument(int documentId, DocumentVerificationDTO verificationDTO);
//	
//	List<DocumentResponseDTO> getDocumentsByLoanIdAndStatus(int loanId, String status);
//	
//	List<DocumentResponseDTO> getDocumentsByLoanOfficerId(int loanOfficerId, String status);
//}
//public interface DocumentService {
//	DocumentResponseDTO uploadDocument(DocumentRequestDTO requestDTO);
//
//	DocumentResponseDTO getDocumentById(int id);
//
//	List<DocumentResponseDTO> getDocumentsByCustomerId(int customerId);
//
//	List<DocumentResponseDTO> getDocumentsByLoanId(int loanId);
//
//	DocumentResponseDTO verifyDocument(int documentId, DocumentVerificationDTO verificationDTO);
//}



import java.util.List;

import com.aurionpro.lending.dto.DocumentRequestDTO;
import com.aurionpro.lending.dto.DocumentResponseDTO;
import com.aurionpro.lending.dto.DocumentVerificationDTO;

public interface DocumentService {
    DocumentResponseDTO uploadDocument(DocumentRequestDTO requestDTO);
    DocumentResponseDTO getDocumentById(int id);
    List<DocumentResponseDTO> getDocumentsByCustomerId(int customerId);
    List<DocumentResponseDTO> getDocumentsByLoanId(int loanId);
    DocumentResponseDTO verifyDocument(int documentId, DocumentVerificationDTO verificationDTO);
    List<DocumentResponseDTO> getDocumentsByLoanIdAndStatus(int loanId, String status);
    List<DocumentResponseDTO> getDocumentsByLoanOfficerId(int loanOfficerId, String status);
    void updateDocumentLoanIds(List<Integer> documentIds, Integer loanId);
}