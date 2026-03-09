package cn.ai.agent.easyaiagent.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RAGConfig {

        /**
         * 当前使用的是text-embedding-v4向量模型
         */
        @Resource
        private EmbeddingModel embeddingModel;

        @Resource
        private EmbeddingStore<TextSegment> embeddingStore;

        /**
         * 该方法的具体作用：以 Spring Bean 的方式构建并暴露一个生产级 RAG 检索器（ContentRetriever），
         * 在应用启动时完成文档加载、精细化分割、向量化摄入，并配置带过滤条件的检索参数。
         *
         * 涉及的底层 AI 概念与 langchain4j 知识点：
         * 1. DocumentSplitter（文档分割器）: 将长文档按指定规则切分为小文本块(TextSegment)。
         * 此处使用 DocumentByLineSplitter，按行分割，每块最多200个token，相邻块重叠10个token。
         * 2. EmbeddingStoreIngestor.builder()（自定义摄入构建器）: 相比简单的静态 ingest() 方法，
         * builder 模式允许自定义 embeddingModel、embeddingStore、documentSplitter 和
         * textSegmentTransformer，实现完整的摄入流水线精细化控制。
         * 3. TextSegmentTransformer（文本段转换器）: 在文本块写入向量库前对其内容做二次加工。
         * 此处将 file_name 元数据前缀拼接到每个文本块，提升向量检索时的语义相关性。
         * 4. EmbeddingModel（向量模型 Bean）: 负责将文本转换为高维向量，此处通过 Spring 注入，
         * 当前使用 text-embedding-v4 向量模型。
         * 5. EmbeddingStoreContentRetriever 检索过滤参数:
         * - maxResults: 限制每次检索最多返回的文档片段数量（此处为1）。
         * - minScore: 向量相似度阈值，过滤掉相关性低于该分值的结果（此处为0.75）。
         *
         * @return 配置完毕的 ContentRetriever Bean，供声明式 AiServices 自动注入使用
         */
        @Bean
        public ContentRetriever contentRetriever() {
                List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/rag_doc");

                // 将每个 Document 拆分为200个token的 TextSegment，最多重叠10个token， 还有其他类型的分割器
                DocumentByLineSplitter documentByLineSplitter = new DocumentByLineSplitter(200, 10);

                // 自定义文档加载器
                EmbeddingStoreIngestor ingest = EmbeddingStoreIngestor.builder()
                                // 将 Document 的名称添加到每个 TextSegment 中以提高搜索质量
                                // 每个 Document 都包含 Metadata。它存储关于 Document 的元信息，例如其名称、来源、上次更新日期、所有者或任何其他相关细节
                                .textSegmentTransformer(textSegment -> TextSegment.from(
                                                textSegment.metadata().getString("file_name") + "\n"
                                                                + textSegment.text(),
                                                textSegment.metadata()))
                                // 向量模型
                                .embeddingModel(embeddingModel)
                                .embeddingStore(embeddingStore)
                                // 文档分割
                                .documentSplitter(documentByLineSplitter).build();
                // 加载文档
                ingest.ingest(documents);
                return EmbeddingStoreContentRetriever.builder()
                                .embeddingModel(embeddingModel)
                                .embeddingStore(embeddingStore)
                                .maxResults(1) // 最多一个结果
                                .minScore(0.75) // 过滤低于0.75的结果
                                .build();
        }
}
