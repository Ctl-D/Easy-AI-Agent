package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.Result;

public interface RAGChatService {

    String chat(String message);

    /**
     * 该方法的具体作用：声明一个返回 Result<String> 的 RAG 对话接口方法，框架会将大模型完整响应元数据封装到 Result 中返回。
     * 涉及的底层 AI 概念与 langchain4j 知识点：
     * Result<T>: 可从中调用 content() 获取文字回答，sources() 获取 RAG 召回的文档来源，tokenUsage() 查看
     * Token 消耗。
     *
     * @param message 用户输入的问题
     * @return Result<String> 包装对象
     */
    Result<String> chatWithResult(String message);
}
