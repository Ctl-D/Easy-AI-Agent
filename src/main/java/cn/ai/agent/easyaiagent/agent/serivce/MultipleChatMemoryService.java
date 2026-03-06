package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

/**
 * <b>注意</b>
 * chatMemoryProvider设置必须配置chatModel 否则会报错
 */
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel", chatMemoryProvider = "dbChatMemoryProvider")
public interface MultipleChatMemoryService {

    /**
     * 该方法的具体作用：提供多用户独立隔离的多轮对话能力。接收用户的唯一标识和消息，自动维护其对应的对话上下文。
     * 涉及到的底层 AI 概念与 langchain4j 知识点：
     * 1. 独立会话记忆区分 (@MemoryId 与
     * ChatMemoryProvider)：在框架中，当需要为不同用户或不同对话窗口保持各自独立的记忆时，必须通过在方法参数上添加 @MemoryId
     * 注解，并在 @AiService 中全局指定 chatMemoryProvider。底层会根据该 ID 动态获取或创建对应的 ChatMemory 实例。
     *
     * @param memoryId 用于区分不同对话上下文的唯一标识（如用户ID或会话ID）
     * @param message  用户输入的提问文本
     * @return AI 在特定上下文记忆下做出的回复
     */
    String chat(@MemoryId String memoryId, @UserMessage String message);
}
