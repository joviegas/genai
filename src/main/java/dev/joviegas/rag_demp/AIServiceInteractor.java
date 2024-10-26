package dev.joviegas.rag_demp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class AIServiceInteractor {

    private final ChatClient chatClient;

    public AIServiceInteractor(ChatClient.Builder builder, VectorStore vectorStore) {
        VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor = new VectorStoreChatMemoryAdvisor(vectorStore);

        this.chatClient = builder
                .defaultAdvisors(vectorStoreChatMemoryAdvisor)
                .build();
    }

    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}