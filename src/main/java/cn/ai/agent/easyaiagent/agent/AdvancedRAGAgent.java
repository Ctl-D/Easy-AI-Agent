package cn.ai.agent.easyaiagent.agent;

import cn.ai.agent.easyaiagent.agent.serivce.AdvancedRAGChatService;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 高级 RAG 可以使用以下核心组件在 LangChain4j 中实现：
 * <p>
 * QueryTransformer (查询转换器)
 * QueryRouter (查询路由器)
 * ContentRetriever (内容检索器)
 * ContentAggregator (内容聚合器)
 * ContentInjector (内容注入器)
 * 下图展示了这些组件如何协同工作：
 * <p>
 * 整个流程如下：
 * <p>
 * 用户生成一个 UserMessage，它被转换为一个 Query。
 * QueryTransformer 将 Query 转换为一个或多个 Query。
 * 每个 Query 由 QueryRouter 路由到一个或多个 ContentRetriever。
 * 每个 ContentRetriever 为每个 Query 检索相关 Content。
 * ContentAggregator 将所有检索到的 Content 组合成一个最终的排名列表。
 * 这个 Content 列表被注入到原始的 UserMessage 中。
 * 最后，包含原始查询和注入的相关内容的 UserMessage 被发送给 LLM。
 */
@Service
public class AdvancedRAGAgent {

    @Resource
    private ChatModel chatModel;
    @Resource(name = "defaultMemory")
    private ChatMemory chatMemory;
    @Resource(name = "llmStructuredOutputRouter")
    private ContentRetriever llmStructuredOutputRouter;

    public String chat(String message) {
        AdvancedRAGChatService agent = AiServices.builder(AdvancedRAGChatService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .contentRetriever(llmStructuredOutputRouter)
                .build();


        return agent.chat(message);
    }

}
