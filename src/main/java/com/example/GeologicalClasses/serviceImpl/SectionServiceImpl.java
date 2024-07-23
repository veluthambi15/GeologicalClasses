package com.example.GeologicalClasses.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GeologicalClasses.entity.GeologicalClass;
import com.example.GeologicalClasses.entity.Section;
import com.example.GeologicalClasses.repository.SectionRepository;
import com.example.GeologicalClasses.service.SectionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SectionServiceImpl implements SectionService {

		@Autowired
	    private final SectionRepository sectionRepository = null;

	    public List<Section> getAllSections() {
	    	// Use the repository to find all sections
	        return sectionRepository.findAll();
	    }

	    public Section getSectionById(Long id) {
	    	 // Use the repository to find a section by its ID, throw an exception if not found
	        return sectionRepository.findById(id).orElseThrow();
	    }

	    public Section createSection(Section section) {
	    	 // Use the repository to save the new section
	        return sectionRepository.save(section);
	    }

	    public Section updateSection(Long id, Section updatedSection) {
	    	// Use the repository to find the existing section by its ID, throw an exception if not found
	    	 Section section = sectionRepository.findById(id)
	    	            .orElseThrow(() -> new EntityNotFoundException("Section not found"));
	    	        // Modify the existing geologicalClasses collection in place
	    	        section.getGeologicalClasses().clear();
	    	        for (GeologicalClass geologicalClass : updatedSection.getGeologicalClasses()) {
	    	            geologicalClass.setSection(section);
	    	            section.getGeologicalClasses().add(geologicalClass);
	    	        }
	    	        return sectionRepository.save(section);
	    }

	    public void deleteSection(Long id) {
	    	// Use the repository to delete the section by its ID
	        sectionRepository.deleteById(id);
	    }

	    public List<Section> getSectionsByGeologicalClassCode(String code) {
	    	// Use the repository to find sections by the geological class code
	        return sectionRepository.findByGeologicalClassesCode(code);
	    }
}

