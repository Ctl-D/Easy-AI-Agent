package cn.ai.agent.easyaiagent.dto;

import dev.langchain4j.rag.content.retriever.ContentRetriever;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 命名检索器
 */
@Data
@AllArgsConstructor
public class NamedRetriever {
    /**
     * 检索器名称
     */
    private String name;

    /**
     * 检索器实例
     */
    private ContentRetriever retriever;
}
