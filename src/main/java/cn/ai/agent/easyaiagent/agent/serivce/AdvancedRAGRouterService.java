package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * 多知识库RAG路由
 */
public interface AdvancedRAGRouterService {

    @SystemMessage("你是一个RAG路由专家，分析用户问题，决定需要查询哪些知识库。")
    @UserMessage("""
            用户问题: {{query}}
            
            可用知识库:
            - basic_info: 基本信息（年龄、出生地、学历、婚姻状况等）
            - jobs: 工作信息（职业、公司、职位、成就等）
            - dreams: 愿望梦想（人生目标、理想等）
            - relationships: 人际关系（家人、朋友、合作关系等）
            
            请返回需要查询的知识库列表。
            例如: ["basic_info", "jobs"] 或 ["dreams"] 等
            """)
    List<String> decideKnowledgeBases(@V("query") String query);
}
