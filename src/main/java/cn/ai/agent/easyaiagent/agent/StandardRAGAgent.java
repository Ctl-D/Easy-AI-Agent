package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StandardRAGAgent {

    @Resource
    private ChatModel chatModel;

    @Resource(name = "defaultMemory")
    private ChatMemory chatMemory;

    @Resource
    private ContentRetriever contentRetriever;

    public String chat(String message) {
        RAGChatService agent = AiServices.builder(RAGChatService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .contentRetriever(contentRetriever)
                .build();
        String chat = agent.chat(message);
        return chat;
    }

    public Result<String> chatWithResult(String message) {
        RAGChatService agent = AiServices.builder(RAGChatService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .contentRetriever(contentRetriever)
                .build();
        return agent.chatWithResult(message);
    }
}
