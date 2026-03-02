package cn.ai.agent.easyaiagent.agent;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FirstSightChatAgent {

    @Resource
    private ChatModel chatModel;

    public String chat(String message) {
        UserMessage userMessage = UserMessage.userMessage(message);
        ChatResponse chatResponse = chatModel.chat(userMessage);
        return aiMessageText(chatResponse);
    }

    /**
     * UserMassage 支持多种类型的消息
     * <p>
     * 参数构造示例：
     * <pre>
     * // 1. 纯文本
     * UserMessage textMessage = UserMessage.from("你好");
     *
     * // 2. 文本 + 图片
     * UserMessage multiModalMessage = UserMessage.from(
     *     TextContent.from("分析这张图片"),
     *     ImageContent.from("https://example.com/image.png")
     * );
     *
     * // 3. 其他类型（如 Audio, Video, File）
     * // 需使用对应的 Content 实现类，例如：AudioContent.from(...), VideoContent.from(...)
     * </pre>
     *
     * @param userMessage 用户消息对象
     * @return AI 回复内容
     */
    public String chatWithUserMessage(UserMessage userMessage) {
        ChatResponse chatResponse = chatModel.chat(userMessage);
        return aiMessageText(chatResponse);
    }

    /**
     * 系统提示词 (System Message)
     * <p>
     * 作用：
     * 1. 设定 AI 的角色（如：你是一个专业的翻译官）
     * 2. 规定 AI 的行为准则（如：只输出翻译结果，不输出其他内容）
     * 3. 提供背景信息或上下文
     * </p>
     *
     * @param message   用户输入的消息
     * @param systemMsg 系统提示词内容
     * @return AI 回复内容
     */
    public String chatWithSystemMessage(String message, String systemMsg) {
        SystemMessage systemMessage = new SystemMessage(systemMsg);
        UserMessage userMessage = UserMessage.userMessage(message);
        ChatResponse chatResponse = chatModel.chat(systemMessage, userMessage);
        return aiMessageText(chatResponse);
    }

    /**
     * 获取 AI 响应的文本内容
     *
     * @param chatResponse 聊天响应对象
     * @return AI 响应的文本内容
     */
    private String aiMessageText(ChatResponse chatResponse) {
        log.info("chatResponse: {}", chatResponse);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("aiMessage: {}", aiMessage);
        return aiMessage.text();
    }
}
