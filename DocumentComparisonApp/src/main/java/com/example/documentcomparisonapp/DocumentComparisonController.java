package com.example.documentcomparisonapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class DocumentComparisonController {

    @Autowired
    private DocumentComparisonService comparisonService;  // Spring will inject this service

    @GetMapping("/")
    public String showForm() {
        return "index"; // Returns Thymeleaf template for file upload
    }

    @PostMapping("/compare")
    public String compareDocuments(@RequestParam("sampleFile") MultipartFile sampleFile,
                                   @RequestParam("uploadedFile") MultipartFile uploadedFile,
                                   Model model) {
        try {
            String sampleFilePath = saveFile(sampleFile);
            String uploadedFilePath = saveFile(uploadedFile);

            // Compare the files using the service
            String comparisonResult = comparisonService.compareDocuments(sampleFilePath, uploadedFilePath);
            model.addAttribute("result", comparisonResult);

        } catch (IOException e) {
            model.addAttribute("error", "Error comparing documents: " + e.getMessage());
        }

        return "index"; // Return to the form page with result
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
        file.transferTo(new File(filePath)); // Save the file temporarily
        return filePath;
    }
}
