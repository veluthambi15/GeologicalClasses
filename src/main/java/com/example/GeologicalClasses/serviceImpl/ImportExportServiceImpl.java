package com.example.GeologicalClasses.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.GeologicalClasses.entity.GeologicalClass;
import com.example.GeologicalClasses.entity.Section;
import com.example.GeologicalClasses.service.ImportExportService;
import com.example.GeologicalClasses.repository.SectionRepository;


@Service
public class ImportExportServiceImpl implements ImportExportService{

	@Autowired
    private SectionRepository sectionRepository;
	
	// Concurrent maps to track the status and results of import/export operations
	private final Map<Long, String> importStatusMap = new ConcurrentHashMap<>();
	private final Map<Long, String> exportStatusMap = new ConcurrentHashMap<>();
	private final Map<Long, ByteArrayResource> exportResultMap = new ConcurrentHashMap<>();
	//private final Map<Long, ByteArrayResource> importResultMap = new ConcurrentHashMap<>();
	private long currentJobId = 0;

	/**
     * Imports sections from an Excel file asynchronously.
     * @param file the Excel file containing sections data
     * @return a CompletableFuture containing the job ID of the import process
     */
    @Async
    public CompletableFuture<Long> importSections(MultipartFile file) {
    	System.out.println("Log 2");
    	 long jobId = generateJobId();
         importStatusMap.put(jobId, "IN PROGRESS");
         System.out.println("Starting import process with Job ID: " + jobId);
         try {
        	 // Read the Excel file
             Workbook workbook = new XSSFWorkbook(file.getInputStream());
             Sheet sheet = workbook.getSheetAt(0);
             List<Section> sections = new ArrayList<>();
             // Iterate through rows and map data to entities
             for (Row row : sheet) {
                 if (row.getRowNum() == 0) continue; // Skip header row
                 Section section = new Section();
                 section.setName(row.getCell(0).getStringCellValue());
                 List<GeologicalClass> geologicalClasses = new ArrayList<>();
                 for (int i = 1; i < row.getLastCellNum(); i += 2) {
                     GeologicalClass geoClass = new GeologicalClass();
                     geoClass.setName(row.getCell(i).getStringCellValue());
                     geoClass.setCode(row.getCell(i + 1).getStringCellValue());
                     geoClass.setSection(section);
                     geologicalClasses.add(geoClass);
                 }
                 section.setGeologicalClasses(geologicalClasses);
                 sections.add(section);
             }
             System.out.println("Saving sections to the database...");
             sectionRepository.saveAll(sections);
             workbook.close();
             //ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
             importStatusMap.put(jobId, "DONE");
             System.out.println("Import process completed successfully for Job ID: " + jobId);
             return CompletableFuture.completedFuture(jobId);
         } catch (IOException e) {
             e.printStackTrace();
             importStatusMap.put(jobId, "ERROR");
             System.out.println("Error during import process for Job ID: " + jobId + ": " + e.getMessage());
             return CompletableFuture.completedFuture(jobId);
         }
    }

    /**
     * Exports sections to an Excel file asynchronously.
     * @return a CompletableFuture containing the job ID of the export process
     */
    @Async
    public CompletableFuture<Long> exportSections() {
    	long jobId = generateJobId();
        exportStatusMap.put(jobId, "IN PROGRESS");
        System.out.println("Starting export process with Job ID: " + jobId);
        try {
        	// Fetch all sections from the database
            List<Section> sections = sectionRepository.findAll();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sections");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Class 1 Name");
            headerRow.createCell(3).setCellValue("Class 1 Code");
            headerRow.createCell(4).setCellValue("Class 2 Name");
            headerRow.createCell(5).setCellValue("Class 2 Code");
            headerRow.createCell(6).setCellValue("Class M Name");
            headerRow.createCell(7).setCellValue("Class M Code");
            
            // Populate rows with section data
            int rowNum = 1;
            for (Section section : sections) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(section.getId());
                row.createCell(1).setCellValue(section.getName());

                int cellNum = 2;
                for (GeologicalClass geoClass : section.getGeologicalClasses()) {
                    row.createCell(cellNum++).setCellValue(geoClass.getName());
                    row.createCell(cellNum++).setCellValue(geoClass.getCode());
                }
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();
            ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());
            exportResultMap.put(jobId, resource);
            exportStatusMap.put(jobId, "DONE");
            System.out.println("Export process completed successfully for Job ID: " + jobId);
            return CompletableFuture.completedFuture(jobId);
        } catch (IOException e) {
            e.printStackTrace();
            exportStatusMap.put(jobId, "ERROR");
            System.out.println("Error during export process for Job ID: " + jobId + ": " + e.getMessage());
            return CompletableFuture.completedFuture(jobId);
        }
    }

    /**
     * Retrieves the status of an import job.
     * @param jobId the ID of the job
     * @return the status of the import job
     */
    public String getImportStatus(Long jobId) {
    	System.out.println("Checking import status for job ID: " + jobId);
        return importStatusMap.getOrDefault(jobId, "UNKNOWN");
    }

    /**
     * Retrieves the status of an export job.
     * @param jobId the ID of the job
     * @return the status of the export job
     */
    public String getExportStatus(Long jobId) {
    	System.out.println("Checking export status for job ID: " + jobId);
        return exportStatusMap.getOrDefault(jobId, "UNKNOWN");
    }

    /**
     * Retrieves the exported file for a given job ID.
     * @param jobId the ID of the job
     * @return the exported file as a ByteArrayResource
     */
    public ByteArrayResource getExportedFile(Long jobId) {
    	ByteArrayResource fileResource = exportResultMap.get(jobId);
        if (fileResource == null) {
            throw new RuntimeException("No file found for Job ID: " + jobId);
        }
        return fileResource;
    }
    
    /**
     * Generates a new job ID in a thread-safe manner.
     * @return the newly generated job ID
     */
    private synchronized long generateJobId() {
        return ++currentJobId;
    }
}

