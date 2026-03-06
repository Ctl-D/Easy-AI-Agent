package cn.ai.agent.easyaiagent.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class DBChatMemory implements ChatMemoryStore {

    ConcurrentHashMap<Object, List<ChatMessage>> memory = new ConcurrentHashMap<>();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        if (memory.containsKey(memoryId)) {
            return memory.get(memoryId);
        }
        return List.of();
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        memory.put(memoryId, messages);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        memory.remove(memoryId);
    }

    public Map<Object, List<ChatMessage>> getMemory() {
        return memory;
    }
}
