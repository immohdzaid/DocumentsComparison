package com.example.documentcomparisonapp;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service  // Add @Service to make this a Spring-managed bean
public class DocumentComparisonService {

    public String compareDocuments(String sampleFilePath, String uploadedFilePath) throws IOException {
        String sampleText = extractTextFromDocx(sampleFilePath);
        String uploadedText = extractTextFromDocx(uploadedFilePath);
        return compareText(sampleText, uploadedText);
    }

    private String extractTextFromDocx(String filePath) throws IOException {
        StringBuilder text = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            XWPFDocument document = new XWPFDocument(fis);
            document.getParagraphs().forEach(paragraph -> text.append(paragraph.getText()).append("\n"));
        } catch (IOException e) {
            throw new IOException("Error reading document: " + filePath, e);
        }
        return text.toString();
    }

    private String compareText(String sampleText, String uploadedText) {
        if (sampleText.equals(uploadedText)) {
            return "The documents are identical.";
        } else {
            return "The documents differ.";
        }
    }
}
