package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

/**
 * <b>注意</b>
 * chatMemory设置必须配置chatModel 否则会报错
 * 单独设置会话记忆
 * 如果使用会话模式需要搭配chatMemoryProvider使用，不然ChatMemoryService实例化不完整，会使用异常空指针
 */
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel", chatMemory = "dbMemory")
public interface MemoryChatService {

    /**
     * 该方法的具体作用：提供基于单一全局记忆库的多轮对话能力。适用于单机单用户或全局共享上下文的简单会话场景。
     * 涉及到的底层 AI 概念与 langchain4j 知识点：
     * 1. 声明式会话记忆绑定 (chatMemory 属性)：在 @AiService 中直接绑定一个具体的 ChatMemory Bean 实例。由于是单个
     * Bean，所有调用该接口的方法都会共享同一套对话上下文（即“单例记忆”）。
     *
     * @param message 用户输入的普通文本
     * @return AI 结合全局会话记忆回复的文本
     */
    String chat(@UserMessage String message);
}
