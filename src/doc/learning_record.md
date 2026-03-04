# LangChain4j 知识点总结

## 1. 基础用户消息 (Basic User Message)
### 概念定义
在大型语言模型交互中，用户消息（User Message）是代表最终用户向模型发送的提问、指令或输入内容的标准数据结构。它是最基础的对话组成部分，用于传递文本形式的用户意图。

### 白话文解释
这就像是你和客服聊天时，你在聊天框里敲下并发送的那句话。它是你主动向 AI 提出的问题或要求，AI 收到这句话后才会给你回复。

### 框架使用示例
在 LangChain4j 中，我们使用 `UserMessage` 类来封装用户发送的文本。

```java
// 入参：message 是用户输入的普通字符串文本，例如 "你好，请问今天天气如何？"
public String chat(String message) {
    // 将普通文本包装成 LangChain4j 框架能理解的 UserMessage 对象
    UserMessage userMessage = UserMessage.userMessage(message);
    // 调用大模型进行对话
    ChatResponse chatResponse = chatModel.chat(userMessage);
    // 出参：从大模型的响应中提取 AI 回复的具体文本内容
    return chatResponse.aiMessage().text();
}
```

## 2. 多模态对话 (Multimodal Chat)
### 概念定义
多模态（Multimodal）是指人工智能系统能够同时处理、理解和生成两种或两种以上不同类型的数据模态（如文本、图像、音频、视频等）的能力。在对话系统中，这意味着输入不仅限于单一的文字，还可以是多种媒体形式的组合。

### 白话文解释
以前咱们给 AI 只能“写信”（纯文字），现在的 AI 长了“眼睛”和“耳朵”。多模态就像是你带了一个聪明的助手去逛街，你不仅可以问他问题（文字），还可以直接指着一件衣服问“这件衣服好看吗？”（图片），或者给他听一段录音让他去翻译（音频）。

### 框架使用示例
在 LangChain4j 中，`UserMessage` 不仅支持纯文本，还可以包含多种内容类型。必须明确指出它支持以下模态组合：
*   **文本 (TextContent)**：常规的文本指令。
*   **图像 (ImageContent)**：可以是本地图片文件、URL 或者是 Base64 编码的图片数据。
*   **音频 (AudioContent)**：声音和语音数据内容。
*   **视频 (VideoContent)**：视频媒体流或文件。
*   **文件等 (PdfFileContent等)**：直接承载 PDF 文档或各类自定义文件载体。

代码示例：
```java
// 入参：userMessage 是一个可以容纳多种数据类型的用户消息对象
public String chatWithUserMessage(UserMessage userMessage) {
    // 调用大模型发送多模态内容（需要底层的大模型本身支持多模态，例如 GPT-4o、Gemini 等）
    ChatResponse chatResponse = chatModel.chat(userMessage);
    // 出参：AI 针对多模态内容的分析和回复文字
    return chatResponse.aiMessage().text();
}

// 构造不同模态 UserMessage 的补充用法展示：
// 1. 纯文本
UserMessage textMsg = UserMessage.from("解释一下量子力学");

// 2. 文本 + 图片 (ImageContent)
UserMessage imgMsg = UserMessage.from(
    TextContent.from("请描述一下这张图片里有什么？"),
    ImageContent.from("https://example.com/sample.jpg")
);

// 3. 文本 + 音频 (AudioContent)
UserMessage audioMsg = UserMessage.from(
    TextContent.from("把这段录音转成文字："),
    AudioContent.from("file:///path/to/audio/test.mp3")
);

// 4. 文本 + 视频 (VideoContent)
UserMessage videoMsg = UserMessage.from(
    TextContent.from("总结这段视频里发生的故事："),
    VideoContent.from("https://example.com/video.mp4")
);
```

## 3. 系统提示词 (System Message)
### 概念定义
系统消息（System Message）是在大语言模型对话上下文中，用于设定模型的行为模式、角色身份、约束条件或全局背景指令的一种特殊消息类型。它通常具有比普通用户提问更高的控制权重，负责在后台引导整个对话按照预期规则进行。

### 白话文解释
这就像是给电影演员发“剧本”或设定“人设”。在用户开始问问题之前，我们先悄悄走后门对 AI 说：“从现在起，你是一个脾气暴躁的厨师，别人问你怎么做菜，你要很不耐烦但又很专业地回答。” 这种不让用户看见但在默默发挥作用的全局规则，就是系统提示词。

### 框架使用示例
在 LangChain4j 中，我们使用 `SystemMessage` 对象来传递系统指令，通常会在发起会话时和 `UserMessage` 一起发送给模型。

```java
// 入参：message 是用户的具体提问，systemMsg 是我们在后台给 AI 预设的角色和规则
public String chatWithSystemMessage(String message, String systemMsg) {
    // 构造系统消息（下发人设、规则）
    SystemMessage systemMessage = new SystemMessage(systemMsg);
    // 构造用户消息（用户面前实际提出的问题）
    UserMessage userMessage = UserMessage.userMessage(message);
    // 将系统规则和用户提问一起发送给大模型处理并得到结果
    ChatResponse chatResponse = chatModel.chat(systemMessage, userMessage);
    // 出参：AI 遵循其人设规则给出的最后答复
    return chatResponse.aiMessage().text();
}
```

## 4. 声明式 AI 服务 (AI Services)
### 概念定义
AI Services 是一种高级声明式（Declarative）编程模型，它将对底层大语言模型（LLM）的复杂调用逻辑（包括请求构造、消息序列化、响应解析、出错重试等）抽象并隐藏起来。开发者只需定义简单的 Java 接口，并通过注解来描述期望的行为即可。

### 白话文解释
这就像是你把一个精通多国语言的万能助手（大语言模型）包装在一个带有特定标签的“处理窗口”后面。你不必每天关心这个助手怎么吃饭、怎么通勤（底层如何构造网络请求、怎么解析返回的 JSON），你只需要在窗口贴个纸条（写个接口），上面写好规则“这是一个专业英语翻译窗口”（使用 `@SystemMessage` 注解），并且可以从外部文件（Resource）把厚厚的“翻译规则说明书”挂上去。然后你只需要把要翻译的词汇递进去，就可以直接拿到翻译结果。这个提供便捷服务的黑盒，就是 AI Service。

### 框架使用示例与不同实现方式
在 LangChain4j 中，我们可以通过简单的接口与 `@SystemMessage` 注解结合来快速构建一个带有特定人设的 Agent。根据项目是否深度集成了 Spring Boot，主要分为以下两种实现方式：

#### 方式一：原生工厂类手动绑定 (编程式创建)
这种方式适用于非 Spring 环境或需要精细控制实例创建过程的场景，需要使用 `AiServices` 工厂类将其与具体的大模型实例进行手动绑定。

```java
// 1. 直写系统提示词
public interface EnglishHelperService {
    @SystemMessage("你是一位专业的单词翻译专家，只能返回翻译后的单词或句子")
    String chat(String message);
}

// 2. 外部加载系统提示词（推荐）
public interface ResourceEnglishHelperService {
    @SystemMessage(fromResource = "systemmessage/english_helper_system_message.txt")
    String chat(String message);
}
```

绑定大模型并生成可调用代理类的工厂方法示例：
```java
@Configuration
public class AIServiceFactory {
    @Resource
    private ChatModel chatModel;

    @Bean
    public EnglishHelperService englishHelperService() {
        // 使用 AiServices 动态代理工厂类，将接口与模型进行自定义绑定
        return AiServices.create(EnglishHelperService.class, chatModel);
    }
}
```

#### 方式二：声明式自动配置 (基于 @AiService 注解)
在 LangChain4j 的 Spring Boot 扩展中，不再需要开发者手动编写工厂类（如上面的 `AIServiceFactory`）去创建和绑定代理实例。而是由框架自动扫描、创建实例并注册到 Spring 容器中。

**白话文解释**：这就好比之前找“万能翻译助手”时，你不仅要写好“窗口（接口）”，还得自己去找一个经理（工厂类），告诉他把这个“窗口”和“助手（大模型）”对接起来。现在有了 `@AiService`，就像有了“一键智能办公系统”。你只需在“窗口”上盖一个 `@AiService` 的公章，系统就会自动帮你把后面的杂活全部接管，你直接拿来用就行了。

代码示例：
```java
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

// 加上 @AiService 注解后，Spring 启动时会自动实现该接口并交由 IOC 容器管理，无需自己编写工厂去创建
@AiService
public interface AIServiceWithFrameworkAnnotationEnglishHelperService {
    @SystemMessage(fromResource = "systemmessage/english_helper_system_message.txt")
    String chat(String message);
}
```

```java
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

// 加上 @AiService 注解后，Spring 启动时会自动实现该接口并交由 IOC 容器管理，无需自己编写 @Configuration 以及工厂去创建
@AiService
public interface AIServiceWithFrameworkAnnotationEnglishHelperService {

    // 同样可以使用之前提到的支持从外部读取复杂的人设和提示词的功能
    @SystemMessage(fromResource = "systemmessage/english_helper_system_message.txt")
    String chat(String message);
}
```

### 架构方案对比：自定义编排会话 vs 注解式声明服务
#### 关联性
在 LangChain4j 中，无论是像 `FirstSightChatAgent` 那样手动组装 `UserMessage`、`SystemMessage` 并调用 `ChatModel`，还是像使用 `@SystemMessage` 和 `@AiService` 那样通过接口声明，它们的底层依然都是向大模型发送对话请求。它们是同一目的的两条不同路径：前者是**命令式 (Imperative)** 调用，后者是**声明式 (Declarative)** 封装。

#### 优缺点分析及适用场景

##### 1. 自定义编排会话方式 (例如: FirstSightChatAgent)
- **优点 (Pros)**：
  - **极高的灵活性与控制力**：可以直接在代码中动态拼接前后台逻辑，随时修改或替换消息体的类型和内容。例如：根据不同的业务条件临时增删某几条历史 `AiMessage`，或动态组装多模态 `ImageContent`。
  - **调试方便**：执行流直观，没有被动态代理包装，方便直接断点跟踪入参和出参流。
- **缺点 (Cons)**：
  - **代码冗长繁琐**：每次交互都需要写大量的包装（如 `UserMessage.userMessage`）和解析代码。
  - **业务耦合**：系统提示词等底层 AI 参数往往跟业务代码混淆在一起，如果有复杂的多轮记忆（Memory）或工具调用（Tools），手动编排的复杂度呈指数级上升。
- **适用场景**：
  - 需要非常复杂或动态变化的消息构建逻辑。
  - 需要在单次请求中精细控制多种不同类型模型交互的底层组件编写。

##### 2. 注解式声明服务 (例如: @SystemMessage / @AiService)
- **优点 (Pros)**：
  - **开发极其便捷**：只需要定义一个接口并添加相关注解，框架自动接管所有的请求构建、响应解析工作。极大减少了样板代码（Boilerplate）。
  - **高解耦与规范化**：通过 `@SystemMessage(fromResource=...)`，可以把动辄几千字的 Prompt 完全隔离到专门的资源文件中，从而提升工程的可维护性。对多轮对话记忆、工具调用的支持也都只需要加上对应注解即可。
- **缺点 (Cons)**：
  - **灵活性受限**：由于被深层封装，很难介入单次请求前后的微小行为干预（例如想要部分动态覆写某次系统提示词或针对个别消息采用特殊的超时控制，就会比较麻烦）。
  - **黑盒效应**：出了问题（比如框架解析特定类型的返回 JSON 失败）时排查和调试门槛较高。
- **适用场景**：
  - 标准化、流程化的 Agent 业务（如客服问答、信息抽取、各类角色助手），尤其是已经有固定输入输出模式的场景。
  - 团队开发中，由架构师配置好底层模型，业务开发人员只需专注接口定义的高效协同模式。
