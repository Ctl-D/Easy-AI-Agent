package cn.ai.agent.easyaiagent.agent;

import cn.ai.agent.easyaiagent.agent.serivce.RAGChatService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EasyRAGAgent {

        @Resource
        private ChatModel chatModel;
        @Resource(name = "defaultMemory")
        private ChatMemory chatMemory;

        /**
         * 该方法的具体作用：基于本地文档的 RAG（检索增强生成）问答，能够根据挂载的本地文件内容回答用户问题。
         * 涉及的底层 AI 概念与 langchain4j 知识点：
         * 1. 检索增强生成 (RAG): 允许大语言模型在回答问题时附加外部文档库信息。
         * 2. Document 加载: 通过 FileSystemDocumentLoader 将物理文件（支持单体、批量、正则匹配）转换为框架的结构化对象。
         * 3. 向量存储与摄入: 利用 EmbeddingStoreIngestor 将文本通过向量化存入本地内嵌库 InMemoryEmbeddingStore
         * 中，供检索器在后续使用。ingest() 方法返回 IngestionResult，可从中获取本次摄入生成的向量条目数量等统计信息。
         * 4. 检索器 (ContentRetriever): 与 AiServices 绑定，在会话时自动从向量数据库召回相关的文本片段补充至上下文中。
         *
         * @param message 用户输入的聊天信息
         * @return 结合文档知识的大语言模型回答
         */
        public String chat(String message) {

                // 加载单个文档 指定返回类型TextDocumentParser，可以不做设置，默认会使用这个
                Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/rag_doc/person_habit.txt",
                                new TextDocumentParser());

                // 加载多个文档
                // List<Document> documents =
                // FileSystemDocumentLoader.loadDocuments("src/main/resources/rag_doc", new
                // TextDocumentParser());

                // 加载目录及其子目录中的所有文档
                // List<Document> documents =
                // FileSystemDocumentLoader.loadDocumentsRecursively("src/main/resources/rag_doc",
                // new TextDocumentParser());
                // 通过使用 glob 或 regex 过滤文档：
                // PathMatcher pathMatcher =
                // FileSystems.getDefault().getPathMatcher("glob:*.pdf");
                // List<Document> documents =
                // FileSystemDocumentLoader.loadDocuments("src/main/resources/rag_doc",
                // pathMatcher,new TextDocumentParser());

                // 文档转换为向量并注入到embeddingStore中
                InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
                IngestionResult ingest = EmbeddingStoreIngestor.ingest(document, embeddingStore);

                RAGChatService agent = AiServices.builder(RAGChatService.class)
                                .chatModel(chatModel)
                                .chatMemory(chatMemory)
                                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                                .build();

                return agent.chat(message);
        }
}
