package cn.ai.agent.easyaiagent.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component("redisChatMemoryProvider")
@ConditionalOnBean(RedisChatMemory.class)
public class RedisChatMemoryProvider implements ChatMemoryProvider {

    private final ConcurrentHashMap<Object, ChatMemory> map = new ConcurrentHashMap<>();

    @Resource
    private RedisChatMemory redisChatMemory;

    @Override
    public ChatMemory get(Object memoryId) {
        if (map.containsKey(memoryId)) {
            return map.get(memoryId);
        }
        MessageWindowChatMemory build = MessageWindowChatMemory.builder()
                .id(memoryId)
                .chatMemoryStore(redisChatMemory)
                .maxMessages(10)
                .build();
        map.put(memoryId, build);
        return build;
    }
}
