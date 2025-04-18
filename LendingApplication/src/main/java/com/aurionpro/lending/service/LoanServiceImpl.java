package com.aurionpro.lending.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.lending.dto.LoanRequestDTO;
import com.aurionpro.lending.dto.LoanResponseDTO;
import com.aurionpro.lending.dto.LoanUpdateDTO;
import com.aurionpro.lending.entity.Customer;
import com.aurionpro.lending.entity.Loan;
import com.aurionpro.lending.entity.LoanOfficer;
import com.aurionpro.lending.entity.LoanScheme;
import com.aurionpro.lending.entity.LoanStatus;
import com.aurionpro.lending.exception.BusinessRuleViolationException;
import com.aurionpro.lending.exception.ResourceNotFoundException;
import com.aurionpro.lending.repository.CustomerRepository;
import com.aurionpro.lending.repository.LoanRepository;
import com.aurionpro.lending.repository.LoanSchemeRepository;
import com.aurionpro.lending.repository.LoanStatusRepository;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanSchemeRepository loanSchemeRepository;

    @Autowired
    private LoanStatusRepository loanStatusRepository;

    @Autowired
    private LoanPaymentService loanPaymentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public LoanResponseDTO applyForLoan(LoanRequestDTO requestDTO) {
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(requestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + requestDTO.getCustomerId()));

        if (customer.isDeleted()) {
            throw new IllegalStateException("Cannot apply for loan: Customer account is deactivated");
        }

        if (customer.getLoanOfficer() == null) {
            throw new BusinessRuleViolationException("Customer must be assigned a Loan Officer before applying for a loan");
        }
        LoanOfficer loanOfficer = customer.getLoanOfficer();

        LoanScheme loanScheme = loanSchemeRepository.findByIdAndIsDeletedFalse(requestDTO.getLoanSchemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan scheme not found with ID: "
                        + requestDTO.getLoanSchemeId() + " or it has been deactivated"));

        LoanStatus status = loanStatusRepository.findByStatusName("PENDING")
                .orElseThrow(() -> new ResourceNotFoundException("Loan status PENDING not found"));

        Loan loan = new Loan();
        loan.setAmount(requestDTO.getAmount());
        loan.setCustomer(customer);
        loan.setLoanOfficer(loanOfficer);
        loan.setLoanScheme(loanScheme);
        loan.setStatus(status);
        loan.setApplicationDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusMonths(loanScheme.getTenureMonths()));

        loan = loanRepository.save(loan);

        return toResponseDTO(loan);
    }

    @Override
    public LoanResponseDTO updateLoanStatus(int loanId, LoanUpdateDTO updateDTO) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        LoanStatus status = loanStatusRepository.findByStatusName(updateDTO.getStatusName())
                .orElseThrow(() -> new ResourceNotFoundException("Loan status not found: " + updateDTO.getStatusName()));
        loan.setStatus(status);

        loan = loanRepository.save(loan);

        if ("APPROVED".equals(updateDTO.getStatusName())) {
            loanPaymentService.createLoanPayments(loanId);
            notificationService.sendLoanStatusEmail(loanId, "approved");
            notificationService.sendInstallmentPlanEmail(loanId);
        } else if ("REJECTED".equals(updateDTO.getStatusName())) {
            notificationService.sendLoanStatusEmail(loanId, "rejected");
        }

        return toResponseDTO(loan);
    }

    @Override
    public LoanResponseDTO getLoanById(int id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));
        return toResponseDTO(loan);
    }

    @Override
    public List<LoanResponseDTO> getLoansByCustomerId(int customerId) {
        List<Loan> loans = loanRepository.findByCustomerId(customerId);
        return loans.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<LoanResponseDTO> getLoansByLoanOfficerId(int loanOfficerId) {
        List<Loan> loans = loanRepository.findByLoanOfficerId(loanOfficerId);
        return loans.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public LoanResponseDTO markLoanAsNpa(int loanId, boolean isNpa) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));
        if (!"APPROVED".equals(loan.getStatus().getStatusName())) {
            throw new IllegalStateException("Only APPROVED loans can be marked as NPA");
        }
        loan.setNpa(isNpa);
        loan = loanRepository.save(loan);
        return toResponseDTO(loan);
    }

    private LoanResponseDTO toResponseDTO(Loan loan) {
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setAmount(loan.getAmount());
        dto.setLoanSchemeName(loan.getLoanScheme() != null ? loan.getLoanScheme().getSchemeName() : null);
        dto.setStatusName(loan.getStatus() != null ? loan.getStatus().getStatusName() : null);
        dto.setApplicationDate(loan.getApplicationDate());
        dto.setDueDate(loan.getDueDate());
        dto.setLoanOfficerId(loan.getLoanOfficer() != null ? loan.getLoanOfficer().getId() : 0);
        dto.setCustomerId(loan.getCustomer() != null ? loan.getCustomer().getId() : 0);
        dto.setNpa(loan.isNpa());
        return dto;
    }
}