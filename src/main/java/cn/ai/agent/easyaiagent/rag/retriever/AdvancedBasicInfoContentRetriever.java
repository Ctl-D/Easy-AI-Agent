package cn.ai.agent.easyaiagent.rag.retriever;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("advancedBasicInfoContentRetriever")
public class AdvancedBasicInfoContentRetriever extends AdvancedBasePeopleContentRetriever {

    public AdvancedBasicInfoContentRetriever(
            @Qualifier("basicInfoEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel) {
        super(embeddingStore, embeddingModel);
    }
}
