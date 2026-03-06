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

>**注意（配置避坑）：多实例架构下的 Bean 注入冲突与 wiringMode**
>在实际项目中，系统可能不仅只有一个大模型或相关配置，而是同时注册了多个不同类型或参数的 Bean。根据 langchain4j 的 Spring Boot 自动装配源码（例如 `AiServicesAutoConfig` 的 `addBeanReference` 机制），当使用 `@AiService` 注解时：
> * 框架默认会采用自动寻找 (`AUTOMATIC`) 模式去装配需要的属性实例（如 ChatModel 等）。
> * 如果上下文中存在**多个符合条件的同一类型实例**（Tools 除外），且你没有明确指定该用哪一个，框架会因为无法抉择而抛出冲突异常并导致应用启动崩溃。
> * **解决方案**：在多实例并存的情况下，必须在服务接口上显式设置装配模式，主动接管控制权，例如：`@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel")`。
> * **极其重要的一点**：一旦设置了 `EXPLICIT`，**你必须明确指定对应的属性（如绑定的模型 bean 名称）**。框架不会再自动去猜测或注入任何底层的能力组件，而是严格按照你在注解中指明的 Bean 名称去按名查找！如果缺少同名的 Bean，应用依然无法启动。

代码示例：
```java
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

// 加上 @AiService 注解后，Spring 启动时会自动实现该接口并交由 IOC 容器管理。
// 这里显式指定了配对使用名为 "qwenChatModel" 的模型 Bean。
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel")
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

## 5. 会话记忆 (ChatMemory)
### 概念定义
大语言模型（LLM）底层接口本质上是无状态的（Stateless），单次调用时它并不知道你在此前发送过什么。**会话记忆（ChatMemory）** 是一种让系统拥有“短期对话记忆”的核心组件机制。它能够在用户每次发出新提问时，自动将历史交流记录（最近的对话上下文）一并打包发送给大模型，从而实现多轮连贯交互。

### 白话文解释
大模型原本就像一个只有“七秒记忆”的接待员，你刚寒暄完名字，下一句问他“你觉得我这个名字怎么样”，他就完全不记得你在说什么了。“会话记忆”就像是给这个接待员配了一个随身记录的“小本本”，每次你开口讲话，系统就会瞬间让他看一遍小本本上你们刚才沟通的文字。这样一来，他就能“回忆”起上文，跟你进行丝滑的连续聊天了。

### 框架使用示例与不同支持类型
在 LangChain4j 框架中，使用 `ChatMemory` 接口管理记忆。考虑到长篇大论会导致上下文超出模型的 Token 处理上限或大幅增加调用成本，系统不仅支持简单的消息记录，还内置了以下几种核心的丢弃（剔除）记忆策略：
*   **按消息数量剔除 (MessageWindowChatMemory)**：简单的滑动窗口策略。只保留最近固定 N 条（N 个 Message）的对话历史摘要，一旦超过设定阈值，最老的对话即被自动遗忘丢弃（但扮演基础人设的 `SystemMessage` 一般会被永久保留）。
*   **按 Token 数量剔除 (TokenWindowChatMemory)**：更安全精确的控制方式。由内置分词器动态计算历史记录的 Token 消耗，确保保留的所有历史加上新问题绝对不会突破大模型的 Token 限制。

代码示例：
```java
@Configuration
public class AIServiceEnglishHelperFactory {
    // 注入底层的大语言模型组件
    @Resource
    private ChatModel chatModel;

    // 为声明式 AI 服务绑定长下文记忆能力
    @Bean
    public AIServiceWithAnnotationResourceEnglishHelperService memoryChatAgentService() {
        // 创建一个基于消息数量的滑动窗口记忆实例，设定最多记住最近的 10 条对话记录
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        
        // 使用 AiServices 的高级构建器 (builder) 将其与大模型、会话记忆统一进行绑定
        return AiServices.builder(AIServiceWithAnnotationResourceEnglishHelperService.class)
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .build();
    }
}
```

## 6. 动态聊天请求与参数配置 (ChatRequest)
### 概念定义
ChatRequest 是一种用于封装单次大语言模型对话请求内容及其专属运行参数的数据结构。通常，我们在初始化一个大语言模型实例时会给定一套全局默认参数（如默认选用的模型版本、默认的随机性散度等）。但在实际业务中，我们经常需要在某一个具体的会话环节临时改变这些设定的参数，而不去影响全局配置。ChatRequest 就是为了满足这种需求而诞生的，它能够携带特定的请求参数覆盖默认配置。

在 LangChain4j 框架中，ChatRequest 允许你传入对话消息集合（Messages）的同时，也可以通过传入 `ChatRequestParameters` 接口的实现类来指定针对这一次对话生效的相关参数，如 `modelName`（模型名称）、`temperature`（随机性/温度）、`maxTokens`（最大输出词元数）等。

### 白话文解释
如果你把大语言模型想象成一个厨师，一开始在长期的合同里你们约定好了他平时做菜放“一勺盐”（默认温度/参数）。但是某天你要招待一个口味比较清淡的客人，你只需要在这份“点菜单”（ChatRequest）上特别备注入一句“这道菜不要放盐”（单次临时调整的参数）。厨师接到这份点菜单，就会**唯独在这道菜上**按照这个临时要求做，而以后其他的菜还是按老规矩“一勺盐”来炒。

### 框架使用示例
在 LangChain4j 中，需要使用 `ChatRequest` 及其底层具体的模型参数实现类（如针对阿里云通义千问的 `QwenChatRequestParameters`，或者针对 OpenAI 的 `OpenAiChatRequestParameters`、Google Gemini 的 `GeminiChatRequestParameters` 等等）来构建包含专属参数的请求，最后调用大模型实例的 `doChat()` 方法。

```java
// 入参：message 是用户想要提出的问题
public String chatWithChatRequest(String message) {
    // 构造常规的用户消息
    UserMessage userMessage = UserMessage.userMessage(message);
    
    // 构造特定模型的请求参数（注意：这一步取决于你底层究竟用的是哪家大模型）
    // 例如这里以 Qwen (通义千问) 为例，动态将本次调用的模型版本指定为 qwen-max，并将温度调整为 0.6
    QwenChatRequestParameters chatRequestParameters = QwenChatRequestParameters.builder()
            .modelName("qwen-max")          // 覆盖单次会话所使用的的模型版本
            .temperature(0.6)               // 覆盖单次会话的温度参数（控制发散度）
            .build();
            
    // 将对话消息与专属参数一起打包，构建最终的 ChatRequest
    ChatRequest chatRequest = ChatRequest.builder()
            .parameters(chatRequestParameters)
            .messages(userMessage)
            .build();
            
    // 调用大模型进行处理，注意！传入 ChatRequest 时，通常使用 doChat() 方法而不是 chat()
    ChatResponse chatResponse = chatModel.doChat(chatRequest);
    
    // 出参：解析并返回 AI 的回答文本
    return chatResponse.aiMessage().text();
}
```

## 7. 持久化会话记忆存储 (Persistent ChatMemoryStore)
### 概念定义
在大型语言模型对话的正常使用中，如果没有特意保存，模型在单次调用后是“无状态”的（Stateless），它并不知道大家在之前发过什么。虽然可以通过 `ChatMemory` 使应用维持短期记忆，但系统默认保存在内存（`InMemory`）中，一旦程序关闭或者服务重启，所有的历史交流上下文会立刻丢失。持久化会话记忆存储（Persistent ChatMemoryStore）则是一种将这段短期的“对话上下文”长期保存到外部数据介质（如数据库、Redis 文件等硬盘环境）的设计模式，为应用赋予即使系统重启仍能读取前文、以及分布式环境（多实例服务）下依然可以互通的跨服务器能力。在此基础上，必须指出在 langchain4j 框架中该特性的实现是将这个接口对象当做底层持久数据存储组件，与负责业务逻辑控制的 `ChatMemory` 相解耦分离。

### 白话文解释
这就像是给原本只拥有“短暂的七秒记忆”并且还会因为下班（服务器关机）立刻失忆的临时接待员，配置了一个带锁的“钢铁档案柜”（例如数据库或 Redis 缓存）。接待员每次陪你聊完天（一次请求结束），都会老老实实地把你们的对话记录写在小纸条上锁进档案柜里。等到明天他又开始上班，即使期间他回了老家（进程重启）甚至换了个长得一模一样的搭档代替他接替这个服务窗口，只要有档案柜存在，他都能翻开读出之前的聊天记录，接着上次没结束的话题顺畅地聊下去。

### 框架使用示例
在 LangChain4j 框架中，`ChatMemoryStore` 是一个专门用于定义“在哪里获取、往哪插入/修改、以及如何删除”聊天记录的存储介质接口，开发者可以轻松自定义它的实现接入任何基础架构。需要明确的是，该概念支持以下相关的具体落库类型拓展组合：
*   **内存暂存 (InMemoryChatMemoryStore)**：即默认的存储在应用自身堆栈中，进程随之消亡。
*   **关系型数据库 (DBChatMemory 等)**：通过编写 SQL 将记忆持久化到了如 MySQL、PostgreSQL 中。
*   **分布式缓存 (RedisChatMemory 等)**：通过 NoSQL 放入内存数据库并能够支撑高并发跨机器的存储方案。
*   **文档式数据库或搜索引擎**：比如 MongoDB 等非关系型库的实现方案。

代码示例：
```java
// 入参：chatMemoryStore 是一个自定义的持久化存储实现 (比如本例中的 DBChatMemory 或 RedisChatMemory)
@Bean("dbMemory")
public ChatMemory dbMemory(DBChatMemory chatMemoryStore) {
    // 构建一个基于消息滑动窗口控制的 ChatMemory
    return MessageWindowChatMemory.builder()
            // 在此显式配置持久化会话记忆组件，将它的底层存储由默认的内部 List 切换到外部介质（如数据库）
            .chatMemoryStore(chatMemoryStore) 
            // 同时仍保留截断老记忆（防止超出 maxToken）的策略机制
            .maxMessages(10)
            .build();
    // 出参：返回一个带有持久化能力、具备完整长短时记忆体系交由核心系统注入给大模型代理后续使用的 ChatMemory 对象
}

// 补充基于 Redis 缓存的实现展示
@Bean("redisMemory")
public ChatMemory redisMemory(RedisChatMemory chatMemoryStore) {
    // 逻辑一模一样：将自定义实现的 RedisChatMemory 注入到内存构造器内，完成独立持久化介质的赋能。
    return MessageWindowChatMemory.builder()
            .chatMemoryStore(chatMemoryStore)
            .maxMessages(10)
            .build();
}
```

## 8. 多用户独立会话记忆区分 (@MemoryId与ChatMemoryProvider)
### 概念定义
当系统需要同时服务成千上万个用户会话时，我们必须为每个不同的聊天窗口或用户维护各自独立的“记忆库”。`@MemoryId` 是一种标识身份的特殊注解，用于从入参中提取当前会话的唯一ID；而 `ChatMemoryProvider` 则是负责接收这些 ID 并根据 ID 动态生成或提取对应的专属会话记忆的提供者机制。

### 白话文解释
如果系统共用一个“记忆本”（单个全局 `ChatMemory`），所有人的聊天记录就会串联，大模型就会“精神错乱”（比如张三问姓名，李四接着问刚才我也来过吗，AI就会混淆两人身份）。
所以我们需要给每个客人都建一个专属档案袋。在声明式服务中，我们在沟通窗口前加上 `@MemoryId` 贴个身份条（“这是张三的记录”），而系统后方的 `chatMemoryProvider` 就负责像智能档案柜一样，看到“张三的条”就瞬间抽出张三的独立对话记录来给 AI 参考。

### 框架使用示例与不同支持类型的配置坑点
在 LangChain4j 的声明式服务（`@AiService`）中，想要使用多会话分离机制，需要特别注意代码中的配置：

>**注意（配置坑点）：在声明多实例与会话模型时的强制约束**
>根据当前核心服务类上的代码警示批注，在为 AI 声明式服务配置会话模型时有极易踩坑的严苛限制：
>1. **前置模型依赖**：不论是使用全局单例的 `chatMemory` 还是动态获取的 `chatMemoryProvider` 属性，必须同时在 `@AiService` 里明确配置其绑定的 `chatModel`（例如 `chatModel = "qwenChatModel"`），否则会导致框架启动解析错乱或报错。
>2. **不完整实例引发的空指针**：如果业务方法中使用了会话模式标识（在其参数上标记了 `@MemoryId` 区分独立对话），那就意味着处于多会话状态。此时接口上**必须**搭配 `chatMemoryProvider` 才能自动按需分配记忆！如果不慎只配置了普通的 `chatMemory`（单例池），会导致底层代理服务 `ChatMemoryService` 实例化不完整，真正发起问答时会直接抛出运行时的空指针异常 (NullPointerException)。

多用户与单用户模式的代码配置对比示例：
```java
// 场景一：单系统用户或全局共享型记忆 (使用 chatMemory)
// 必须配置 chatModel；因没有 @MemoryId，只需通过 chatMemory 属性绑定一个全局持久化的记忆库实例
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemory = "dbMemory")
public interface MemoryChatService {
    String chat(@UserMessage String message);
}

// 场景二：多用户隔离型动态记忆（使用 chatMemoryProvider）
// 方法参数中使用了 @MemoryId 来绑定用户身份，这里接口必须强制使用 chatMemoryProvider 属性！
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemoryProvider = "dbChatMemoryProvider")
public interface MultipleChatMemoryService {
    // 调用时通过 memoryId 隔离出专属于这个会话的历史上下文，再合并进大模型的提问中
    String chat(@MemoryId String memoryId, @UserMessage String message);
}
```

## 9. 结构化输出 (Structured Outputs / JSON Schema)
### 概念定义
结构化输出（Structured Outputs），在底层常表现为强制大模型遵循 JSON Schema 规范返回数据，是一种让大语言模型从生成不可控的自由文本（Free-form Text）转变为生成严格符合预定义数据结构（如特定的 JSON 对象或属性数组）的技术能力。它彻底解决了在工程应用中依赖正则表达式或容易出错的字符串截取来“猜”和“抠” AI 回复信息的痛点。

### 白话文解释
以前我们让 AI 帮忙从一段简历里找出候选人的姓名和年龄，AI 可能会回答：“经过我的仔细分析，这位候选人的名字叫张三，今年25岁了哦。” 这种回答对于人来说很好懂，但对于写代码的程序员来说简直是噩梦，因为程序没法直接把这段话存进数据库。
“结构化输出”就像是咱们给 AI 发了一张严谨的“填空申请表”。我们不仅给它一段文字，还命令它：“别跟我废话讲长篇大论，必须严格按照 表格字段一：姓名，表格字段二：年龄 的格式给我填好交表！” 这样 AI 返回给我们的就直接是一个可以直接录入系统数据库的纯粹的数据包（JSON），程序拿到手就能用，一丁点废话都没有。

### 框架使用示例
在 LangChain4j 框架中，我们只要在声明式引擎 `@AiService` 下将方法的**返回值（Return Type）直接定义为你想要的 POJO、Record 或 Java 实体类**，框架就会神不知鬼不觉地在底层自动截获请求，把这个 Java 类的结构（属性类型和字段名）反向编译成 JSON Schema，塞进发给大模型的提示词里强制它遵循，并且在得到大模型回复后，自动将其反序列化成 Java 对象丢给你。一切都非常顺滑！

除了单个实体类，LangChain4j 同样支持返回**基础类型**（如 `boolean`、`int`）、**枚举 (Enum)**，甚至是**复杂的集合包装类型**（如 `List<Person>`、`Map<String, Object>`）。

代码示例：
```java
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

// 1. 定义期望输出的数据结构模型（推荐使用 Record 一层搞定字段声明）
public record Person(Integer age, String name, String country, String birth) {}

// 2. 声明式定义 AI 解析服务
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel")
public interface JsonSchemaService {
    
    // 我们在这里加上人设规则，比如“从用户的描述中提取任务属性信息”
    @SystemMessage(fromResource = "systemmessage/person_schema_message.text")
    // 关键核心：将返回值直接指向我们的结构体 Person 而不是 String。
    Person chat(String message);
}
```

### 进阶特性：字段级指令增强 (@Description)
#### 概念定义
在某些复杂的业务场景下，我们要提取的实体对象字段名称（如英文简写的变量名 `phylum`, `genus`）对大模型来说可能有歧义，或者需要它按照极其特定的格式返回数据。`@Description` 注释允许我们在 Java 对象的类级别或字段级别附加额外的自然语言提示，这些额外说明会被 LangChain4j 直接解析并转换为底层的 JSON Schema 描述（description），从而精准引导大模型该怎么填这个“格子”。

#### 白话文解释
这就像是我们给“填空申请表”加上了“防呆小字说明”。光写一个表头“门类”可能有人会填“防盗门”，我们在旁边用小括号补充一行说明 `@Description("生物学分类，如脊索动物门")`，别人填表的时候就不会出这种乌龙了，大模型也是一样。

#### 框架使用示例
在 LangChain4j 中，无论是常规的 POJO 还是 Record，都可以直接在类名或属性字段上打上 `@Description` 注解（**注意：在导包时请务必认准引用的框架原生注解 `dev.langchain4j.model.output.structured.Description`，千万不要让 IDE 误导入 `jdk.jfr.Description`**）。

代码示例：
```java
// 正确引入 LangChain4j 的 Description 注解
import dev.langchain4j.model.output.structured.Description;

// 在类级别整体说明这个对象的用途
@Description("动物的专业分类信息")
public record Animal(
        @Description("动物通用名称，如：东北虎、非洲狮")
        String name,
        
        @Description("门：生物分类的主要门类，如：脊索动物门")
        String phylum,
        
        @Description("保护等级：极危CR、濒危EN、易危VU、无危LC等")
        String conservationStatus
) {}

// 后续在 @AiService 中直接返回 Animal 即可，底层会自动将上述约束字典喂给 AI 服务。
```
