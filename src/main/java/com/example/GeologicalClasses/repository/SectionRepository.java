package com.example.GeologicalClasses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GeologicalClasses.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByGeologicalClassesCode(String code);
}

