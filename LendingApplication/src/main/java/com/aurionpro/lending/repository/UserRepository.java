package com.aurionpro.lending.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.lending.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmailAndIsDeletedFalse(String email);

	Optional<User> findByUsernameAndIsDeletedFalse(String username);

	Optional<User> findByIdAndIsDeletedFalse(Integer id);

	@Query("SELECT u FROM User u WHERE (:includeDeleted = true OR u.isDeleted = false)")
	List<User> findAllUsers(boolean includeDeleted);

	@Modifying
	@Query("UPDATE User u SET u.isDeleted = :isDeleted WHERE u.id = :id")
	void updateIsDeletedById(Integer id, boolean isDeleted);
}