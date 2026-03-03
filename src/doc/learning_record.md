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
