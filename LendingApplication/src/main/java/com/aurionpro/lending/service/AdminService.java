package com.aurionpro.lending.service;

import java.util.List;

import com.aurionpro.lending.dto.AdminResponseDTO;

public interface AdminService {
	AdminResponseDTO getAdminById(int id);

	List<AdminResponseDTO> getAllAdmins();
}