package com.example.GeologicalClasses.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface ImportExportService {

	public CompletableFuture<Long> importSections(MultipartFile file);
	public CompletableFuture<Long> exportSections();
	public String getImportStatus(Long jobId);
	public String getExportStatus(Long jobId);
	public ByteArrayResource getExportedFile(Long jobId);
}
