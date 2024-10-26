package dev.joviegas.migration;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public class DocumentSplitter {

    private static final int MAX_TOKENS = 10000; // Maximum tokens per chunk
    private static final int MIN_CHUNK_SIZE = 10000; // Minimum chunk size to split documents

    // Method to split a document from its content into text segments
    public static List<TextSegment> splitDocument(String content) {
        // Use LangChain DocumentSplitter to split content into segments
        return DocumentSplitters.recursive(MIN_CHUNK_SIZE, MAX_TOKENS)
                .split(new Document(content));
    }

    // Utility method for testing, simulating the old file reading
    public static String readFile(String filePath) throws Exception {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(filePath)));
    }
}
