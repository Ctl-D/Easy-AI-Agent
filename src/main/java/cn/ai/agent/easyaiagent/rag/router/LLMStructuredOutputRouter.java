package cn.ai.agent.easyaiagent.rag.router;

import cn.ai.agent.easyaiagent.agent.serivce.AdvancedRAGRouterService;
import cn.ai.agent.easyaiagent.dto.NamedRetriever;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 大模型决定调用哪个知识库
 */
@Component("llmStructuredOutputRouter")
@Slf4j
public class LLMStructuredOutputRouter implements ContentRetriever {
    private final Map<String, NamedRetriever> namedRetrieverMap;
    private final AdvancedRAGRouterService routerAIService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public LLMStructuredOutputRouter(ChatModel chatModel,
                                     @Qualifier("defaultMemory") ChatMemory chatMemory,
                                     List<NamedRetriever> allRetrievers) {
        log.info("Initializing LLMStructuredOutputRouter with {} retrievers", allRetrievers.size());
        allRetrievers.forEach(r -> log.info(" - Available retriever: {}", r.getName()));

        this.namedRetrieverMap = allRetrievers.stream()
                .collect(Collectors.toMap(NamedRetriever::getName, nr -> nr));
        this.routerAIService = AiServices.builder(AdvancedRAGRouterService.class)
                .chatMemory(chatMemory)
                .chatModel(chatModel)
                .build();
    }

    @Override
    public List<Content> retrieve(Query query) {
        log.info("Processing query: {}", query.text());

        try {
            // 1. 让大模型决定需要哪些知识库
            List<String> targetKBs = routerAIService.decideKnowledgeBases(query.text());
            log.info("LLM selected knowledge bases: {}", targetKBs);

            if (targetKBs == null || targetKBs.isEmpty()) {
                log.info("No specific knowledge bases selected, retrieving from all");
                return retrieveFromAll(query);
            }

            // 过滤出存在的知识库
            List<String> validKBs = targetKBs.stream()
                    .filter(kb -> {
                        boolean exists = namedRetrieverMap.containsKey(kb);
                        if (!exists) {
                            log.warn("Knowledge base '{}' not found", kb);
                        }
                        return exists;
                    })
                    .collect(Collectors.toList());

            if (validKBs.isEmpty()) {
                log.warn("No valid knowledge bases found, falling back to all");
                return retrieveFromAll(query);
            }

            // 2. 并行检索选中的知识库
            log.info("Retrieving from {} knowledge bases: {}", validKBs.size(), validKBs);
            List<Content> results = retrieveParallel(validKBs, query);
            log.info("Retrieved total {} contents", results.size());

            return results;

        } catch (Exception e) {
            log.error("Error in retrieve: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private List<Content> retrieveParallel(List<String> targetKBs, Query query) {
        List<CompletableFuture<List<Content>>> futures = targetKBs.stream()
                .map(name -> {
                    ContentRetriever retriever = namedRetrieverMap.get(name).getRetriever();
                    log.debug("Submitting retrieval task for: {}", name);

                    return CompletableFuture.supplyAsync(() -> {
                        long startTime = System.currentTimeMillis();
                        try {
                            List<Content> results = retriever.retrieve(query);
                            long duration = System.currentTimeMillis() - startTime;
                            log.debug("Retrieved {} results from {} in {}ms",
                                    results.size(), name, duration);
                            return results;
                        } catch (Exception e) {
                            log.error("Error retrieving from {}: {}", name, e.getMessage());
                            return List.<Content>of();
                        }
                    }, executorService);
                })
                .collect(Collectors.toList());

        return futures.stream()
                .map(future -> {
                    try {
                        return future.join();
                    } catch (Exception e) {
                        log.error("Error joining future: {}", e.getMessage());
                        return List.<Content>of();
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Content> retrieveFromAll(Query query) {
        log.info("Retrieving from all {} knowledge bases", namedRetrieverMap.size());

        return namedRetrieverMap.values().stream()
                .map(NamedRetriever::getRetriever)
                .flatMap(retriever -> {
                    try {
                        return retriever.retrieve(query).stream();
                    } catch (Exception e) {
                        log.error("Error in fallback retrieval: {}", e.getMessage());
                        return List.<Content>of().stream();
                    }
                })
                .collect(Collectors.toList());
    }
}


