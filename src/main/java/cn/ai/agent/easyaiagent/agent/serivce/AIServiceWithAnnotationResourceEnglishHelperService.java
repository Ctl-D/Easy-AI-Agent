package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.SystemMessage;

public interface AIServiceWithAnnotationResourceEnglishHelperService {

    /**
     * 该方法的具体作用：接收用户输入的文本，后台基于外部配置文件中设定的规则（如英文翻译专家）进行处理并返回结果。
     * 涉及到的 AI 概念与 langchain4j 知识点：
     * 1. 声明式 AI 服务 (AI Services)：基于接口动态代理，将繁琐的底层大模型调用封装为标准 Java 方法调用。
     * 2. 外部化系统提示词加载 (@SystemMessage(fromResource))：将长篇的提示词（Prompt）提取到独立文本文件中（如
     * resources/systemmessage），实现代码与 Prompt 逻辑的分离和解耦。
     *
     * @param message 用户输入的文本
     * @return 经过 AI 模型按照预设 Prompt 处理后返回的结果
     */
    @SystemMessage(fromResource = "systemmessage/english_helper_system_message.txt")
    String chat(String message);
}
