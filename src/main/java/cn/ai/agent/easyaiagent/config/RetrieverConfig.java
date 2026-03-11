package cn.ai.agent.easyaiagent.config;

import cn.ai.agent.easyaiagent.dto.NamedRetriever;
import cn.ai.agent.easyaiagent.rag.retriever.AdvancedBasicInfoContentRetriever;
import cn.ai.agent.easyaiagent.rag.retriever.AdvancedPeopleDreamsContentRetriever;
import cn.ai.agent.easyaiagent.rag.retriever.AdvancedPeopleJobsContentRetriever;
import cn.ai.agent.easyaiagent.rag.retriever.AdvancedPeopleRelationsShipsContentRetriever;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 初始化知识检索器
 */
@Configuration
public class RetrieverConfig {

    @Bean
    public NamedRetriever basicInfoNamedRetriever(
            AdvancedBasicInfoContentRetriever retriever) {
        return new NamedRetriever("basic_info", retriever);
    }

    @Bean
    public NamedRetriever jobsNamedRetriever(
            AdvancedPeopleJobsContentRetriever retriever) {
        return new NamedRetriever("jobs", retriever);
    }

    @Bean
    public NamedRetriever dreamsNamedRetriever(
            AdvancedPeopleDreamsContentRetriever retriever) {
        return new NamedRetriever("dreams", retriever);
    }

    @Bean
    public NamedRetriever relationshipsNamedRetriever(
            AdvancedPeopleRelationsShipsContentRetriever retriever) {
        return new NamedRetriever("relationships", retriever);
    }

    @Bean
    public List<NamedRetriever> allNamedRetrievers(
            NamedRetriever basicInfoNamedRetriever,
            NamedRetriever jobsNamedRetriever,
            NamedRetriever dreamsNamedRetriever,
            NamedRetriever relationshipsNamedRetriever) {
        return List.of(
                basicInfoNamedRetriever,
                jobsNamedRetriever,
                dreamsNamedRetriever,
                relationshipsNamedRetriever
        );
    }
}