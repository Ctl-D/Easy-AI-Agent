package cn.ai.agent.easyaiagent.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 创建四大知识库的EmbeddingStore
 */
@Configuration
public class EmbeddingStoreConfig {

    @Bean("defaultEmbeddingStore")
    @Primary
    public EmbeddingStore<TextSegment> defaultEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean("basicInfoEmbeddingStore")
    public EmbeddingStore<TextSegment> basicInfoEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean("jobsEmbeddingStore")
    public EmbeddingStore<TextSegment> jobsEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean("dreamsEmbeddingStore")
    public EmbeddingStore<TextSegment> dreamsEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean("relationshipsEmbeddingStore")
    public EmbeddingStore<TextSegment> relationshipsEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
}