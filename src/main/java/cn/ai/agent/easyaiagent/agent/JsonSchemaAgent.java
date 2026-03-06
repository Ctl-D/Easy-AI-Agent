package cn.ai.agent.easyaiagent.agent;

import cn.ai.agent.easyaiagent.systemmessage.PersonSchemaMessage;
import dev.langchain4j.community.model.dashscope.QwenChatRequestParameters;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 结构化输出
 */
@Service
@Slf4j
public class JsonSchemaAgent {

    @Resource
    private ChatModel chatModel;

    /**
     * 没有定义record person的原始方式 目前最可靠，精度最高的结构化输出
     *
     * @param message
     * @return
     */
    public String chat(String message) {
        SystemMessage systemMessage = SystemMessage.from(PersonSchemaMessage.SYSTEM_MESSAGE);
        UserMessage userMessage = UserMessage.userMessage(message);
        //person结构
        JsonObjectSchema schema = JsonObjectSchema.builder()
                .addStringProperty("name")
                .addIntegerProperty("age")
                .addStringProperty("country")
                .addStringProperty("birth")
                .required("name", "country") //必要字段
                .build();

        JsonSchema jsonSchema = JsonSchema.builder()
                .name("person")
                .rootElement(schema)
                .build();
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(jsonSchema)
                .build();

        QwenChatRequestParameters chatRequestParameters = QwenChatRequestParameters.builder()
                .modelName("qwen-max")
                .responseFormat(responseFormat)
                .build();

        ChatRequest chatRequest = ChatRequest.builder()
                .parameters(chatRequestParameters)
                .messages(systemMessage, userMessage)
                .build();
        ChatResponse chatResponse = chatModel.doChat(chatRequest);
        return chatResponse.aiMessage().text();
    }
}
