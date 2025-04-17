package com.aurionpro.lending.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.lending.entity.NpaStatus;

public interface NpaStatusRepository extends JpaRepository<NpaStatus, Integer> {
	Optional<NpaStatus> findByStatusName(String statusName);
}