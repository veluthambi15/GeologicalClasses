package com.example.GeologicalClasses.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GeologicalClasses.entity.Section;
import com.example.GeologicalClasses.service.SectionService;


@RestController
@RequestMapping("/sections")
public class SectionController {

	 	@Autowired
	    private SectionService sectionService;

	 	/**
	     * Retrieves a list of all sections.
	     * @return List of Section objects.
	     */
	    @GetMapping
	    public List<Section> getAllSections() {
	        return sectionService.getAllSections();
	    }

	    /**
	     * Retrieves a specific section by its ID.
	     * @param id ID of the section to retrieve.
	     * @return Section object with the specified ID.
	     */
	    @GetMapping("/{id}")
	    public Section getSectionById(@PathVariable Long id) {
	        return sectionService.getSectionById(id);
	    }

	    /**
	     * Creates a new section.
	     * @param section Section object to create.
	     * @return The created Section object.
	     */
	    @PostMapping
	    public Section createSection(@RequestBody Section section) {
	        return sectionService.createSection(section);
	    }

	    /**
	     * Updates an existing section by its ID.
	     * @param id ID of the section to update.
	     * @body section Updated Section object.
	     * @return The updated Section object.
	     */
	    @PutMapping("/{id}")
	    public Section updateSection(@PathVariable Long id, @RequestBody Section section) {
	        return sectionService.updateSection(id, section);	
	    }

	    /**
	     * Deletes a section by its ID.
	     * @param id ID of the section to delete.
	     * @return ResponseEntity with no content.
	     */
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
	        sectionService.deleteSection(id);
	        return ResponseEntity.noContent().build();
	    }

	    /**
	     * Retrieves sections based on a geological class code.
	     * @param code Geological class code to filter sections.
	     * @return List of Section objects matching the specified code.
	     */
	    @GetMapping("/by-code")
	    public List<Section> getSectionsByGeologicalClassCode(@RequestParam String code) {
	        return sectionService.getSectionsByGeologicalClassCode(code);
	    }
}
