package cn.ai.agent.easyaiagent.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 创建知识库配置类
 */
@Configuration
public class PeopleKnowledgeBaseConfig {

    private final ResourceLoader resourceLoader;

    public PeopleKnowledgeBaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Document basicInfoDocument() throws IOException {
        return load("classpath:rag_doc/advanced/people_basic.txt");
    }

    @Bean
    public Document jobsDocument() throws IOException {
        return load("classpath:rag_doc/advanced/people_jobs.txt");
    }

    @Bean
    public Document dreamsDocument() throws IOException {
        return load("classpath:rag_doc/advanced/people_dreams.txt");
    }

    @Bean
    public Document relationshipsDocument() throws IOException {
        return load("classpath:rag_doc/advanced/people_relationships.txt");
    }

    private Document load(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        InputStream inputStream = resource.getInputStream();
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return Document.from(content);
    }

    @Bean
    public EmbeddingStoreIngestor basicInfoIngestor(
            EmbeddingModel embeddingModel,
            @Qualifier("basicInfoEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public EmbeddingStoreIngestor jobsIngestor(EmbeddingModel embeddingModel,
                                               @Qualifier("jobsEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public EmbeddingStoreIngestor dreamsIngestor(EmbeddingModel embeddingModel
            , @Qualifier("dreamsEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public EmbeddingStoreIngestor relationshipsIngestor(EmbeddingModel embeddingModel
            , @Qualifier("relationshipsEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }
}
