package dev.joviegas.migration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PromptEngineer {

    private static final String MIGRATION_INSTRUCTION =
            "Migrate the following method from AWS SDK Java 1.x to AWS SDK Java 2.x. " +
                    "Replace all AWS SDK Java 1.x method calls with the corresponding AWS SDK Java 2.x methods. " +
                    "If a pom.xml file is provided, add AWS SDK Java 2.x dependencies but do not remove existing dependencies. " +
                    "Provide the migrated code only, without including explanations or comments about the changes made:\n";

    private static final String ADDITIONAL_INSTRUCTION_HEADER =
            "\n\nIMPORTANT: Please ensure the following instructions are addressed:\n";



    public static String createPrompt(String code) {
        String basePrompt = "Migrate the following method from AWS SDK Java 1.x to AWS SDK Java 2.x. " +
                "Replace all SDK calls and ensure compatibility with the context provided. " +
                "Provide the migrated code and any specific migration instructions if applicable.Also at the end give best practices related to that newly added aws sdk 2.x if available. The code is as follows:\n" + code;

        if (code.contains("AmazonSQSBufferedAsyncClient")) {
            String instructionsFilePath = "src/main/resources/docs/PromptEngBatchInstruction.txt";
            try {
                String additionalInstructions = Files.readString(Paths.get(instructionsFilePath));
                return basePrompt + ADDITIONAL_INSTRUCTION_HEADER + additionalInstructions;
            } catch (IOException e) {
                e.printStackTrace();
                return basePrompt + "\n\n(Note: Additional instructions could not be loaded due to an error.)";
            }
        }

        return basePrompt;
    }
}
