# learning_record.md 知识点索引

> ⚠️ 此文件由 `finish_extk.js` 脚本自动维护，请勿手动修改章节行。

| 章节号 | 知识点名称 | 关键词/类名标签 |
|--------|-----------|----------------|
| 1 | 基础用户消息 (Basic User Message) | `UserMessage`, `ChatResponse`, `chatModel.chat` |
| 2 | 多模态对话 (Multimodal Chat) | `TextContent`, `ImageContent`, `AudioContent`, `VideoContent`, `PdfFileContent` |
| 3 | 系统提示词 (System Message) | `SystemMessage`, `chatModel.chat` |
| 4 | 声明式 AI 服务 (AI Services) | `@AiService`, `@SystemMessage`, `AiServices`, `AiServiceWiringMode`, `wiringMode`, `EXPLICIT` |
| 5 | 会话记忆 (ChatMemory) | `ChatMemory`, `MessageWindowChatMemory`, `TokenWindowChatMemory` |
| 6 | 动态聊天请求与参数配置 (ChatRequest) | `ChatRequest`, `ChatRequestParameters`, `QwenChatRequestParameters`, `doChat` |
| 7 | 持久化会话记忆存储 (Persistent ChatMemoryStore) | `ChatMemoryStore`, `InMemoryChatMemoryStore`, `DBChatMemory`, `RedisChatMemory` |
| 8 | 多用户独立会话记忆区分 (@MemoryId与ChatMemoryProvider) | `@MemoryId`, `ChatMemoryProvider`, `chatMemoryProvider`, `MultipleChatMemoryService` |
| 9 | 结构化输出 (Structured Outputs / JSON Schema) | `@Description`, `JsonObjectSchema`, `ResponseFormat`, `ResponseFormatType`, `JsonSchema` |
| 10 | 检索增强生成 (RAG) | `FileSystemDocumentLoader`, `EmbeddingStoreIngestor`, `InMemoryEmbeddingStore`, `EmbeddingStoreContentRetriever`, `ContentRetriever`, `IngestionResult`, `TokenUsage` |
| 11 | RAG 生产级精细化配置 | `DocumentSplitter`, `DocumentByLineSplitter`, `DocumentSplitters`, `TextSegmentTransformer`, `EmbeddingStoreIngestor.builder`, `maxResults`, `minScore` |
| 12 | AI 服务结果包装类 (Result) | `Result`, `content()`, `sources()`, `tokenUsage()`, `finishReason()`, `FinishReason` |

