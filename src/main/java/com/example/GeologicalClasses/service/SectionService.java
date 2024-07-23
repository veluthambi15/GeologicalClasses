package com.example.GeologicalClasses.service;

import java.util.List;

import com.example.GeologicalClasses.entity.Section;

public interface SectionService {
	
	public List<Section> getAllSections();
	public Section getSectionById(Long id);
	public Section createSection(Section section);
	public Section updateSection(Long id, Section updatedSection);
	public void deleteSection(Long id);
	public List<Section> getSectionsByGeologicalClassCode(String code);

}
