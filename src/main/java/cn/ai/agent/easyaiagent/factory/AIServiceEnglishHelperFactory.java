package cn.ai.agent.easyaiagent.factory;

import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationEnglishHelperService;
import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationResourceEnglishHelperService;
import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithFrameworkAnnotationEnglishHelperService;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于AI Service创建实例
 */
@Configuration
public class AIServiceEnglishHelperFactory {

    @Resource
    private ChatModel chatModel;

    @Bean
    public AIServiceWithAnnotationEnglishHelperService aiServiceEnglishHelperService() {
        return AiServices.create(AIServiceWithAnnotationEnglishHelperService.class, chatModel);
    }

    @Bean
    public AIServiceWithAnnotationResourceEnglishHelperService aiServiceWithAnnotationResourceEnglishHelperService() {
        return AiServices.create(AIServiceWithAnnotationResourceEnglishHelperService.class, chatModel);
    }

    /**
     * 该方法的具体作用：为声明式 AI 服务构建并绑定一个带有“短期记忆”能力的聊天配置实例，设置其最大记忆消息轮数以供多轮对话使用。
     * 涉及到的 AI 概念与 langchain4j 知识点：
     * 1. 会话记忆 (ChatMemory)：让无状态的大语言模型能够在一定上下文中“记住”之前的对话历史，支持多轮连贯交互。
     * 2. 滑动窗口记忆 (MessageWindowChatMemory)：一种常见记忆策略，仅保留最近 N 条消息，自动丢弃更早的内容以节省 Token。
     *
     * @return 已经绑定了大模型和会话记忆能力的 Service 代理对象
     */
    @Bean
    public AIServiceWithAnnotationResourceEnglishHelperService memoryChatAgentService() {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        return AiServices.builder(AIServiceWithAnnotationResourceEnglishHelperService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory).build();
    }
}
