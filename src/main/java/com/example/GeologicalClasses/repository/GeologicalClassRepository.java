package com.example.GeologicalClasses.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GeologicalClasses.entity.GeologicalClass;

public interface GeologicalClassRepository extends JpaRepository<GeologicalClass, Long> {
	
}
