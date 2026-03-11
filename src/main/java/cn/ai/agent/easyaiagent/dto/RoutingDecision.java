package cn.ai.agent.easyaiagent.dto;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

import java.util.List;

@Data
public class RoutingDecision {

    @Description("需要检索的知识库名称列表")
    private List<String> targetKnowledgeBases;

    @Description("检索策略：PARALLEL 或 SEQUENTIAL")
    private String strategy;

    @Description("每个知识库返回的结果数量")
    private Integer resultCount;

    @Description("决策理由")
    private String reasoning;
}
