package dev.joviegas.rag_demp;


import dev.joviegas.migration.PromptEngineer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import dev.joviegas.migration.PromptEngineer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private String extractedJavaCode;  // To store the extracted Java code temporarily

    public ChatController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new VectorStoreChatMemoryAdvisor(vectorStore))
                .build();
    }

    @PostMapping("/convert")
    public String chat(@RequestParam("file") MultipartFile file) {
        try {
            // Get the original file path
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                return "Error: Invalid file name";
            }

            // Read the file content as a string
            String fileContent = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Send the file content to the chatClient for conversion
            String convertedContent = chatClient.prompt()
                    .user(PromptEngineer.createPrompt(fileContent))
                    .call()
                    .content();

            // Extract Java code and explanation
            this.extractedJavaCode = extractJavaCode(convertedContent);
            String explanation = extractExplanationText(convertedContent);

            // Provide an option for the user to upload a path for saving the Java code
            return "<html><body>" +
                    "<h3>Key Changes Made:</h3>" +
                    "<p>" + explanation + "</p>" +
                    "<form method='POST' action='/saveConvertedFile' enctype='multipart/form-data'>" +
                    "Please upload a file path where the Java code should be saved:<br>" +
                    "<input type='text' name='savePath' placeholder='Enter file path'><br>" +
                    "<button type='submit'>Save Java Code</button>" +
                    "</form>" +
                    "</body></html>";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing file: " + e.getMessage();
        }
    }

    @PostMapping("/saveConvertedFile")
    public String saveConvertedFile(@RequestParam("savePath") String savePath) {
        try {
            if (this.extractedJavaCode == null) {
                return "Error: No Java code to save. Please upload a file first.";
            }

            // Save the extracted Java code to the specified path
            Path path = Paths.get(savePath);
            Files.write(path, extractedJavaCode.getBytes(StandardCharsets.UTF_8));

            return "Java code successfully saved to: " + path.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error saving Java code: " + e.getMessage();
        }
    }

    private String extractJavaCode(String content) {
        // Regex to match content between ```java and ```
        Pattern pattern = Pattern.compile("```java\\s+(.*?)```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1) : "Java code not found.";
    }

    private String extractExplanationText(String content) {
        // Extract text following the Java code
        Pattern pattern = Pattern.compile("```.*?```\\s*(.*)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1).replace("\n", "<br>") : "Explanation text not found.";
    }
}
