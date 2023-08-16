package com.blatant.api.controller;

import com.blatant.api.dto.AddFileRequest;
import com.blatant.api.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/file")
public class FileController {
    
    private final FileService fileService;
    Logger log = LoggerFactory.getLogger(FileController.class);
    
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    @PostMapping("/admin/add")
    public ResponseEntity<?> addFile(@RequestBody AddFileRequest request) {
        try {
            fileService.addFile(request);
            log.info("Add file success: {}", request);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.error("Add file error:", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/downloads/client")
    public void downloadClientZip(HttpServletResponse response) throws FileNotFoundException {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=download.zip");
            response.setStatus(HttpServletResponse.SC_OK);
            
            List<String> fileNames = Stream.of(Objects.requireNonNull(ResourceUtils.getFile("classpath:client").listFiles()))
                    .filter(file -> !file.isDirectory())
                    .map(File::getAbsolutePath)
                    .toList();
            
            try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
                for (String file : fileNames) {
                    FileSystemResource resource = new FileSystemResource(file);
                    
                    ZipEntry e = new ZipEntry(Objects.requireNonNull(resource.getFilename()));
                   
                    e.setSize(resource.contentLength());
                    e.setTime(System.currentTimeMillis());
                 
                    zippedOut.putNextEntry(e);
                    StreamUtils.copy(resource.getInputStream(), zippedOut);
                    zippedOut.closeEntry();
                }
                zippedOut.finish();
            }
            catch (Exception e) {
                log.error("Download loader error:", e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
    }
}