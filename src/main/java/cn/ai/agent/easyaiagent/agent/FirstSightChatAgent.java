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

    /**
     * 与大模型进行简单对话交互
     * <p>
     * 知识点：基础用户消息 (Basic User Message)
     * 将普通字符串包装为 langchain4j 能识别的 UserMessage，这是最基础的文本对话形式。
     * </p>
     * 
     * @param message 用户输入的文本消息
     * @return AI 回复的文本内容
     */
    public String chat(String message) {
        UserMessage userMessage = UserMessage.userMessage(message);
        ChatResponse chatResponse = chatModel.chat(userMessage);
        return aiMessageText(chatResponse);
    }

    /**
     * 发送包含多种类型内容的复杂消息给 AI（支持多模态）
     * <p>
     * 知识点：多模态对话 (Multimodal Chat)
     * UserMessage 不仅支持纯文本，还可以包含图片 (ImageContent)、音频 (AudioContent) 等多种模态的内容。
     * </p>
     * <p>
     * 参数构造示例：
     * 
     * <pre>
     * // 1. 纯文本
     * UserMessage textMessage = UserMessage.from("你好");
     *
     * // 2. 文本 + 图片
     * UserMessage multiModalMessage = UserMessage.from(
     *         TextContent.from("分析这张图片"),
     *         ImageContent.from("https://example.com/image.png"));
     *
     * // 3. 其他类型（如 Audio, Video, File）
     * // 需使用对应的 Content 实现类，例如：AudioContent.from(...), VideoContent.from(...)
     * </pre>
     *
     * @param userMessage 用户消息对象（可以是纯文本，也可以是多模态内容组合）
     * @return AI 回复内容
     */
    public String chatWithUserMessage(UserMessage userMessage) {
        ChatResponse chatResponse = chatModel.chat(userMessage);
        return aiMessageText(chatResponse);
    }

    /**
     * 带有系统设定（人设）的对话交互
     * <p>
     * 知识点：系统提示词 (System Message)
     * 系统提示词就像给 AI 设定的“剧本”或“人设”。
     * 作用：
     * 1. 设定 AI 的角色（如：你是一个专业的翻译官）
     * 2. 规定 AI 的行为准则（如：只输出翻译结果，不输出解释）
     * 3. 提供对话的全局背景信息或上下文
     * </p>
     *
     * @param message   用户输入的提问内容
     * @param systemMsg 给 AI 设定的系统提示词（人设/规则）
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
