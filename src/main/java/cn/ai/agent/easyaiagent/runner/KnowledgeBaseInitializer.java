package cn.ai.agent.easyaiagent.runner;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KnowledgeBaseInitializer implements CommandLineRunner {

    @Resource
    private Document basicInfoDocument;
    @Resource
    private Document jobsDocument;
    @Resource
    private Document dreamsDocument;
    @Resource
    private Document relationshipsDocument;

    @Resource
    private EmbeddingStoreIngestor basicInfoIngestor;
    @Resource
    private EmbeddingStoreIngestor jobsIngestor;
    @Resource
    private EmbeddingStoreIngestor dreamsIngestor;
    @Resource
    private EmbeddingStoreIngestor relationshipsIngestor;

    /**
     * 该方法的具体作用：实现 CommandLineRunner 接口的 run() 方法，在 Spring Boot 应用启动完成后
     * 自动触发，将多个分主题的文档分别注入到对应的命名 EmbeddingStoreIngestor 中，完成知识库初始化。
     *
     * 涉及的底层 AI 概念与 langchain4j 知识点：
     * 1. CommandLineRunner（Spring 启动钩子）: Spring Boot 提供的接口，实现类在应用上下文
     * 完全准备好后自动执行，是 RAG 系统在启动时一次性完成文档摄入的标准实现位置。
     * 2. 多知识库隔离摄入: 为不同主题的文档配置独立命名的 EmbeddingStoreIngestor Bean，
     * 每个 Ingestor 可绑定不同的 EmbeddingStore/EmbeddingModel/Splitter，实现主题隔离。
     * 3. Document Bean 注入: 将文档对象本身注册为 Spring Bean，方便在各组件间共享和注入。
     *
     * @param args Spring Boot 传入的命令行参数（通常不使用）
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting knowledge base initialization...");

        log.info("Ingesting basic info document...");
        basicInfoIngestor.ingest(basicInfoDocument);

        log.info("Ingesting jobs document...");
        jobsIngestor.ingest(jobsDocument);

        log.info("Ingesting dreams document...");
        dreamsIngestor.ingest(dreamsDocument);

        log.info("Ingesting relationships document...");
        relationshipsIngestor.ingest(relationshipsDocument);

        log.info("Knowledge base initialization completed!");
    }
}