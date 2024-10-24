package dev.joviegas.rag_demp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @GetMapping("/")
    public String chat() {

        String javaCode = "clientConfig.setConnectionTimeout(5000); // Connection timeout in milliseconds\n" +
                "clientConfig.setSocketTimeout(5000); // Socket timeout in milliseconds\n" +
                "clientConfig.setMaxErrorRetry(3); // Number of retries for failed requests\n" +
                "clientConfig.setProxyHost(\"proxy.example.com\"); // Proxy host, if needed\n" +
                "clientConfig.setProxyPort(8080); // Proxy port, if needed\n" +
                "\n" +
                "// Create an AmazonS3 client with the custom ClientConfiguration\n" +
                "AmazonS3 s3Client = AmazonS3ClientBuilder.standard()\n" +
                "        .withClientConfiguration(clientConfig)\n" +
                "        .build();";

        return chatClient.prompt()
                .user("How do you convert a statement  to aws sdk java 2.x , just give me converted lines , try your best , just give me converted form , donot give me instruction on how to do it , just convert it" +javaCode)
                .call()
                .content();
    }
}