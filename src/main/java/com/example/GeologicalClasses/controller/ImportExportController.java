package com.example.GeologicalClasses.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.GeologicalClasses.serviceImpl.ImportExportServiceImpl;

@RestController
@RequestMapping("")
public class ImportExportController {
	    
	    @Autowired
	    private ImportExportServiceImpl importExportService;
	    
	    /**
	     * Endpoint to handle file import requests.
	     * Initiates the import process and returns a Job ID upon success.
	     * Returns an error message with Job ID on failure.
	     */
	    @PostMapping("/import")
	    public ResponseEntity<String> importSections(@RequestParam("file") MultipartFile file) {
	    	 Long jobId = null; 
	    	try {
	    		 CompletableFuture<Long> futureJobId = importExportService.importSections(file);
	    		 jobId = futureJobId.get();
	             return ResponseEntity.ok("File imported successfully. Job ID: " + jobId);
	         } catch (Exception e) {
	             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File import failed. Job ID: " + jobId);
	         }
	    }
	    
	    /**
	     * Endpoint to retrieve the status of an import job.
	     * Takes the job ID as a path variable and returns the current status.
	     * Helps users track the progress of their import request.
	     */
	    @GetMapping("/import/{id}")
	    public String getImportStatus(@PathVariable Long id) {
	    	return "Import status for id " + id + ": " + importExportService.getImportStatus(id);
	    }

	    /**
	     * Endpoint to initiate the export of sections data.
	     * Returns a Job ID indicating the export process has started.
	     * Users can track the export process using this Job ID.
	     */
	    @GetMapping("/export")
	    public String exportSections() {
	        return "Export id is : " + importExportService.exportSections().join();
	    }

	    /**
	     * Endpoint to retrieve the status of an export job.
	     * Takes the job ID as a path variable and returns the current status.
	     * Useful for tracking the progress of data export requests.
	     */
	    @GetMapping("/export/{id}")
	    public String getExportStatus(@PathVariable Long id) {
	    	return "Export status for id " + id + ": " + importExportService.getExportStatus(id);
	    }

	    /**
	     * Endpoint to download the exported file.
	     * Takes the job ID as a path variable and returns the file if the export is complete.
	     * Sets appropriate headers for file download, including content disposition and length.
	     */
	    @GetMapping("/export/{id}/file")
	    public ResponseEntity<ByteArrayResource> getExportedFile(@PathVariable Long id) {
	        ByteArrayResource file = importExportService.getExportedFile(id);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sections.xlsx")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .contentLength(file.contentLength())
	                .body(file);
	    }

}
