package cn.ai.agent.easyaiagent.agenttest;

import cn.ai.agent.easyaiagent.agent.FirstSightChatAgent;
import cn.ai.agent.easyaiagent.systemmessage.EnglishHelperSystemMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FirstSightChatAgentTest {

    @Resource
    private FirstSightChatAgent agent;

    @Test
    public void test() {
        agent.chat("1+1等于多少？");
    }

    @Test
    public void imageTest() {
        UserMessage message = UserMessage.from(TextContent.from("描述图片"),
                ImageContent.from("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQgoTvFmMsBPLgx0Sk0Rdve25kNiDmv8yrl6Wrr7hfxFg&s"));
        agent.chatWithUserMessage(message);
    }

    @Test
    public void systemMessageTest() {
        agent.chatWithSystemMessage("Advance", EnglishHelperSystemMessage.SYSTEM_MESSAGE);
    }


    @Test
    public void chatWithChatRequestTest() {
        String message = agent.chatWithChatRequest("你是谁？");
        System.out.println(message);
    }
}
