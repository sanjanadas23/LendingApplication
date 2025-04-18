package com.aurionpro.lending.service;

import com.aurionpro.lending.dto.ProfileResponseDTO;
import com.aurionpro.lending.dto.ProfileUpdateRequestDTO;

public interface ProfileService {
	ProfileResponseDTO updateProfile(int userId, ProfileUpdateRequestDTO requestDTO);

	ProfileResponseDTO updateCustomerProfile(int customerId, ProfileUpdateRequestDTO requestDTO);

	ProfileResponseDTO updateLoanOfficerProfile(int loanOfficerId, ProfileUpdateRequestDTO requestDTO);

	ProfileResponseDTO updateAdminProfile(int adminId, ProfileUpdateRequestDTO requestDTO);

	ProfileResponseDTO getProfileByUserId(int userId);

	ProfileResponseDTO getProfileByCustomerId(int customerId);

	ProfileResponseDTO getProfileByLoanOfficerId(int loanOfficerId);

	ProfileResponseDTO getProfileByAdminId(int adminId);
}