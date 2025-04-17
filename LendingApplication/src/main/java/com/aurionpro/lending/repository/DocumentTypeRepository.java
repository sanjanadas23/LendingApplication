package com.aurionpro.lending.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.lending.entity.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
	Optional<DocumentType> findByTypeName(String typeName);
}