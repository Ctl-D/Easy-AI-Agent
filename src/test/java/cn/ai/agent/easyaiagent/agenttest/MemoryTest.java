package cn.ai.agent.easyaiagent.agenttest;

import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationResourceEnglishHelperService;
import cn.ai.agent.easyaiagent.agent.serivce.MemoryChatService;
import cn.ai.agent.easyaiagent.agent.serivce.MultipleChatMemoryService;
import cn.ai.agent.easyaiagent.memory.DBChatMemory;
import com.alibaba.dashscope.utils.JsonUtils;
import dev.langchain4j.data.message.ChatMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class MemoryTest {

    @Resource
    private AIServiceWithAnnotationResourceEnglishHelperService memoryChatAgentService;

    @Resource
    private MemoryChatService memoryChatService;

    @Resource
    private DBChatMemory dbChatMemory;

    @Resource
    private MultipleChatMemoryService multipleChatMemoryService;

    @Test
    public void memoryTest() {
        memoryChatAgentService.chat("orange");
        memoryChatAgentService.chat("apple");
        memoryChatAgentService.chat("peach");
        memoryChatAgentService.chat("egg");
        System.out.println(memoryChatAgentService.chat("我第三次问的单词是什么，回答的记录是什么"));
    }

    @Test
    public void memoryTest2() {
        memoryChatService.chat("1+1等于几？");

        memoryChatService.chat("2+2等于几？");

        memoryChatService.chat("3+3等于几？");

        memoryChatService.chat("上海有什么好玩的地方?");
        Map<Object, List<ChatMessage>> memory = dbChatMemory.getMemory();
        System.out.println(JsonUtils.toJson(memory));

    }

    @Test
    public void memoryTest3() {
        String sessionId1 = UUID.randomUUID().toString().replace("-", "");
        multipleChatMemoryService.chat(sessionId1, "1+1等于几？");

        multipleChatMemoryService.chat(sessionId1, "2+2等于几？");

        multipleChatMemoryService.chat(sessionId1, "3+3等于几？");
        String sessionId2 = UUID.randomUUID().toString().replace("-", "");

        multipleChatMemoryService.chat(sessionId2, "3-1等于几?");
        Map<Object, List<ChatMessage>> memory = dbChatMemory.getMemory();
        System.out.println(JsonUtils.toJson(memory));

    }

}
