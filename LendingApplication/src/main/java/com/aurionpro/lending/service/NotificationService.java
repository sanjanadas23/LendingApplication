package com.aurionpro.lending.service;

import java.math.BigDecimal;

import com.aurionpro.lending.entity.Document;

public interface NotificationService {
	void sendLoanStatusEmail(int loanId, String status);

	void sendPaymentReminderEmail(int loanPaymentId);

	void sendInstallmentPlanEmail(int loanId);

	void sendPaymentConfirmationEmail(int loanPaymentId, BigDecimal amountPaid);

	void sendNpaPendingNotificationToOfficer(int loanId, int loanOfficerId);

	void sendNpaNotificationToCustomer(int customerId, int loanId, BigDecimal totalDues);

	void sendDocumentRejectionEmail(int loanId, Document document);
}