package cn.ai.agent.easyaiagent.config;

import cn.ai.agent.easyaiagent.memory.DBChatMemory;
import cn.ai.agent.easyaiagent.memory.RedisChatMemory;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {

    @Bean("defaultMemory")
    public ChatMemory defaultMemory() {
        return MessageWindowChatMemory.builder().maxMessages(10).build();
    }

    /**
     * 构建基于数据库持久化的会话记忆实例。
     * 相关 AI 与框架概念：持久化会话记忆存储 (Persistent ChatMemoryStore)
     * 在 langchain4j 默认情况下，对话历史保存在内存中，重启即丢失。此方法通过将自定义的 DBChatMemory
     * 对象通过 builder 的 chatMemoryStore() 方法注入，让 ChatMemory 拥有将上下文长期保存到数据库
     * 的持久化能力。同时依旧使用 MessageWindowChatMemory 限制最近记录的最大消息窗口。
     */
    @Bean("dbMemory")
    public ChatMemory dbMemory(DBChatMemory chatMemoryStore) {
        return MessageWindowChatMemory.builder().chatMemoryStore(chatMemoryStore).maxMessages(10).build();
    }

    /**
     * 构建基于 Redis 持久化的会话记忆实例。
     * 相关 AI 与框架概念：持久化会话记忆存储 (Persistent ChatMemoryStore)
     * 在 langchain4j 默认情况下，对话历史保存在内存中，重启即丢失。此方法通过将自定义的 RedisChatMemory
     * 对象通过 builder 的 chatMemoryStore() 方法注入，让 ChatMemory 拥有将上下文长期保存到 Redis
     * 的分布式持久化能力。同时也运用滑动窗口机制限制最大消息数。
     */
    @Bean("redisMemory")
    public ChatMemory redisMemory(RedisChatMemory chatMemoryStore) {
        return MessageWindowChatMemory.builder().chatMemoryStore(chatMemoryStore).maxMessages(10).build();
    }

}
