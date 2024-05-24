package ru.akvine.web_crawler.rest.converters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class WebCrawlerConverter {
    public ResponseEntity convertToExportFileResponse(Map<Integer, Set<String>> tree) {
        String filename = "output-" + new Date().toString().replace(" ", "-").replace(":", "") + ".txt";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/txt"))
                .body(convertTreeToFile(tree));
    }

    private byte[] convertTreeToFile(Map<Integer, Set<String>> tree) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream))) {
            for (Map.Entry<Integer, Set<String>> entry : tree.entrySet()) {
                writer.write(entry.getKey() + ": " + String.join(", ", entry.getValue()));
                writer.newLine();
            }
        } catch (Exception exception) {
            throw new RuntimeException("Error while export values to text file, message = " + exception.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }
}
