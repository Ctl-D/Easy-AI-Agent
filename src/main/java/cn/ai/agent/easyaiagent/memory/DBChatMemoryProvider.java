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
@Component("dbChatMemoryProvider")
@ConditionalOnBean(DBChatMemory.class)
public class DBChatMemoryProvider implements ChatMemoryProvider {

    private final ConcurrentHashMap<Object, ChatMemory> map = new ConcurrentHashMap<>();

    @Resource
    private DBChatMemory dbChatMemory;

    @Override
    public ChatMemory get(Object memoryId) {
        if (map.containsKey(memoryId)) {
            return map.get(memoryId);
        }

        MessageWindowChatMemory build = MessageWindowChatMemory.builder()
                .id(memoryId)
                .chatMemoryStore(dbChatMemory)
                .maxMessages(10) //dbChatMemory注入的实例，这个值只控制一次会话中的最大对话数，多个会话上限相互不影响
                .build();
        map.put(memoryId, build);
        return build;
    }
}
