package dev.joviegas.migration;

import dev.joviegas.rag_demp.AIServiceInteractor;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class AwsSdkMigrationTool {

    private final AIServiceInteractor aiTransformer;

    @Autowired
    public AwsSdkMigrationTool(AIServiceInteractor aiTransformer) {
        this.aiTransformer = aiTransformer;
    }

    public void transform(String inputFile, String outputFile) throws Exception {
        // Step 1: Read the content of the input Java file
        String content = DocumentSplitter.readFile(inputFile);

        // Step 2: Split the content into smaller chunks using DocumentSplitter
        List<TextSegment> chunks = DocumentSplitter.splitDocument(content);

        StringBuilder processedContent = new StringBuilder();

        // Step 3: Process each chunk through the AI model
        for (TextSegment chunk : chunks) {


            // Call the AI transformation logic (chat interaction or improvements)
            String improvedCode = aiTransformer.chat(PromptEngineer.createPrompt(chunk.text()));

            processedContent.append(improvedCode).append("\n");
        }

        // Step 4: Write the processed content to the output file
        Files.write(Paths.get(outputFile), processedContent.toString().getBytes());

        System.out.println("Processing complete. Output saved to " + outputFile);
    }

}
