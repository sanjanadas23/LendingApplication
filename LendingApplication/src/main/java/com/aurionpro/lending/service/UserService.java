package com.aurionpro.lending.service;

import java.util.List;

import com.aurionpro.lending.dto.JwtResponseDTO;
import com.aurionpro.lending.dto.LoginRequestDTO;
import com.aurionpro.lending.dto.UserRequestDTO;
import com.aurionpro.lending.dto.UserResponseDTO;

public interface UserService {
	UserResponseDTO registerUser(UserRequestDTO requestDTO, String roleName);

	UserResponseDTO getUserById(int id);

	JwtResponseDTO login(LoginRequestDTO loginRequestDTO);

	List<UserResponseDTO> getAllUsers(boolean includeDeleted);
	
//	JwtResponseDTO generateTestToken(int userId, String username, String roleName);
}