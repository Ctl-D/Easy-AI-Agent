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
         * 该方法的具体作用：通过原始的、编程式的方法手动构建极其精细的 JSON Schema
         * 结构约束，并将其作为大模型的响应格式参数，强制要求模型返回符合该严格定义的 JSON 字符串。
         * 涉及到的 AI 概念与 langchain4j 知识点：
         * 1. 编程式结构化输出 (Programmatic JSON Schema / ResponseFormat)：相比于通过定义 Record
         * 类的声明式做法，使用 JsonObjectSchema.builder() 这种底层 API 能够获得目前最高精度、最细粒度的控制权（比如精确指定必填项
         * required() 等）。它将构造出的模型参数放入 ChatRequestParameters 的 ResponseFormat 中发送给大模型。
         * 
         * 注意（模型适配性警示）：目前类似于 Qwen 等部分国产大模型的底层在适配上不支持强制性的 JSON_SCHEMA responseFormat
         * 字段。如果强行注入，会在校验阶段由底层抛出 `UnsupportedFeatureException("JSON response format is
         * not supported")` 异常，必须依靠单纯的 Prompt 约束或退回声明式服务解析来解决（详见源码注释）。
         *
         * @param message 包含业务实体的自然语言文本
         * @return 符合手动构建的严格 JSON Schema 断言的纯 JSON 格式字符串
         */
        public String chat(String message) {
                SystemMessage systemMessage = SystemMessage.from(PersonSchemaMessage.SYSTEM_MESSAGE);
                UserMessage userMessage = UserMessage.userMessage(message);
                // person结构
                JsonObjectSchema schema = JsonObjectSchema.builder()
                                .addStringProperty("name")
                                .addIntegerProperty("age")
                                .addStringProperty("country")
                                .addStringProperty("birth")
                                .required("name", "country") // 必要字段
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
