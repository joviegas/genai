package dev.joviegas.rag_demp;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.*;

import org.springframework.ai.document.Document;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IngestionService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IngestionService.class);
    private final VectorStore vectorStore;


    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }



    @Override
    public void run(String... args) throws Exception {
        // Use Spring's ResourcePatternResolver to load all files in the docs/ directory
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/docs/*");  // Adjust this pattern as necessary

        // Process each file found in the directory
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                log.info("Processing file: " + resource.getFilename());

                // Parse the document using Apache Tika
                List<Document> parsedDocuments = parseDocumentWithTika(resource);

                // Use your existing TextSplitter logic
                TextSplitter textSplitter = new TokenTextSplitter();

                // Store the processed text in the VectorStore
                vectorStore.accept(textSplitter.apply(parsedDocuments));
            } else {
                log.warn("Cannot read file: " + resource.getFilename());
            }
        }

        log.info("VectorStore Loaded with all data from docs directory!");
    }

    // Modify this method to accept the Resource and read it directly
    private List<Document> parseDocumentWithTika(Resource resource) throws IOException, TikaException, SAXException {
        List<Document> documents = new ArrayList<>();

        // Use the Resource's input stream
        try (InputStream inputStream = resource.getInputStream()) {
            // Using Tika to extract the content
            Tika tika = new Tika();
            String fileContent = tika.parseToString(inputStream);

            // Split the content into sections/paragraphs or process it as needed
            String[] sections = fileContent.split("\n\n"); // Splitting into sections by double newline (paragraphs)

            // Create metadata (optional, can be replaced with actual metadata if required)
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("source", "Tika");

            // Convert each section into a Document object
            for (String section : sections) {
                // Choose the appropriate constructor for Document
                Document document = new Document(section, metadata);  // Or another appropriate constructor
                documents.add(document);
            }
        }

        return documents;  // Return a List of Document objects
    }
}

//    @Override
//    public void run(String... args) throws Exception {
//
//        var pdfReader = new ParagraphPdfDocumentReader(marketPDF);
//        TextSplitter textSplitter = new TokenTextSplitter();
//        vectorStore.accept(textSplitter.apply(pdfReader.get()));
//        log.info("VectorStore Loaded with data!");
//    }
