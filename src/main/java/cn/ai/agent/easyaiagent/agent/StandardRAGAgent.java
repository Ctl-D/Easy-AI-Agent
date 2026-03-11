package cn.ai.agent.easyaiagent.agent;

import cn.ai.agent.easyaiagent.agent.serivce.RAGChatService;
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

    @Resource(name = "standardRagContentRetriever")
    private ContentRetriever contentRetriever;

    /**
     * 该方法的具体作用：注入 Spring Bean 形式的 ContentRetriever 构建 RAG 对话代理，发起标准 RAG 对话。
     * 涉及的底层 AI 概念与 langchain4j 知识点：
     * 1. ContentRetriever Bean 注入: 检索器由 Spring 容器统一管理，避免每次对话重复初始化文档摄入流程，是生产级 RAG
     * 的标准做法。
     * 2. AiServices 动态代理: 框架自动在 chat() 调用前执行检索器召回，将相关文档上下文拼入 Prompt。
     *
     * @param message 用户输入的问题
     * @return 大模型结合文档上下文生成的文本回答
     */
    public String chat(String message) {
        RAGChatService agent = AiServices.builder(RAGChatService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .contentRetriever(contentRetriever)
                .build();
        String chat = agent.chat(message);
        return chat;
    }

    /**
     * 该方法的具体作用：与 chat() 相同发起 RAG 对话，但返回值使用 Result<String> 包装，
     * 可从中获取 AI 回答文本之外的元数据信息，如来源文档、TokenUsage、完结原因等。
     * 涉及的底层 AI 概念与 langchain4j 知识点：
     * 1. Result<T>（AI 服务结果包装类）: 声明式 AI 服务方法可将返回类型声明为 Result<T>，
     * 框架会将大模型的完整响应元数据一并封装返回，而不仅仅是纯文本回答。
     *
     * @param message 用户输入的问题
     * @return Result<String> 包装对象，content() 获取文本，sources() 获取召回文档来源，tokenUsage() 获取
     * Token 消耗
     */
    public Result<String> chatWithResult(String message) {
        RAGChatService agent = AiServices.builder(RAGChatService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .contentRetriever(contentRetriever)
                .build();
        return agent.chatWithResult(message);
    }
}
