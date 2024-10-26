package dev.joviegas.rag_demp;

import dev.joviegas.migration.PromptEngineer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SnippetController {

    private final ChatClient chatClient;

    public SnippetController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new VectorStoreChatMemoryAdvisor(vectorStore))
                .build();
    }

    @PostMapping("/processSnippet")
    public String processSnippet(@RequestParam("snippet") String snippet, Model model) {
        try {
            // Send the user-supplied snippet to the chatClient for processing
            String processedContent = chatClient.prompt()
                    .user(PromptEngineer.createPrompt(PromptEngineer.createPrompt(snippet)))
                    .call()
                    .content();

            // Format the processed content (if necessary)
            String formattedResponse = formatCodeBlocks(processedContent);

            // Add the formatted response to the model
            model.addAttribute("processedContent", formattedResponse);

            // Return the name of the Thymeleaf template
            return "processedSnippet";  // Make sure to create a processedSnippet.html template in 'templates' folder

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error processing snippet: " + e.getMessage());
            return "error";  // You should have an 'error.html' template as well for handling errors
        }
    }


    // Utility method to handle formatting of code blocks in the response
    private String formatCodeBlocks(String content) {
        // Regex to match code blocks between ```java and ```
        return content.replaceAll("```java\\s+(.*?)```", "<code>$1</code>")
                .replace("\n", "<br>");
    }

    @GetMapping("/snippet")
    public String snippetform() {
        return "snippetform";  // This tells Spring to load snippetform.html from the templates directory
    }
}
