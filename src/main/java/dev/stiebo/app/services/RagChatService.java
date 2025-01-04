package dev.stiebo.app.services;

import dev.stiebo.app.dtos.DocumentDto;
import dev.stiebo.app.dtos.UploadFileInputStreamResource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class RagChatService {
    private final VectorStore vectorStore;
    private final JdbcClient jdbcClient;
    private final ChatClient chatClient;

    public RagChatService(VectorStore vectorStore, JdbcClient jdbcClient, ChatClient.Builder builder,
                          ResourceLoader resourceLoader) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
        Resource resourceSystemPrompt =
                resourceLoader.getResource("classpath:/prompts/chatclientsystemprompt.st");
        this.chatClient = builder
                .defaultSystem(resourceSystemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    private boolean existsByDocumentName(String documentName) {
        return jdbcClient.sql(
                        "SELECT EXISTS (SELECT 1 FROM vector_store WHERE metadata->>'source' = :document_name)")
                .param("document_name", documentName)
                .query(Boolean.class)
                .single();
    }

    public void addDocument(UploadFileInputStreamResource resource) {
        if (existsByDocumentName(resource.getFilename())) {
            throw new RuntimeException("Document with that name already exists in database");
        }
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        List<Document> documentList = new TokenTextSplitter().apply(reader.read());
        vectorStore.add(documentList);
    }

    public List<DocumentDto> listDocuments() {
        return jdbcClient.sql(
                        "SELECT DISTINCT metadata->>'source' AS documentName " +
                                "FROM vector_store " +
                                "WHERE metadata->>'source' IS NOT NULL")
                .query(DocumentDto.class).list();
    }

    public void deleteDocument(DocumentDto documentDto)  {
        if (!existsByDocumentName(documentDto.documentName())) {
            throw new RuntimeException("Document not found in database");
        }
        jdbcClient.sql("DELETE FROM vector_store WHERE metadata->>'source' = :document_name")
                .param("document_name", documentDto.documentName())
                .update();
    }

    public Flux<String> chat(String question) {
        return chatClient.prompt()
                .user(question)
                .stream()
                .content();
    }

}
