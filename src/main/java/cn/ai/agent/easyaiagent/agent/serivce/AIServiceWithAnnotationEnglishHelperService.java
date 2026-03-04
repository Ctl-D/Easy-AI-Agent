package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.SystemMessage;

public interface AIServiceWithAnnotationEnglishHelperService {

    /**
     * 该方法的具体作用：作为一个专业的单词翻译专家，接收用户输入的文本并返回翻译结果。
     * 涉及到的 AI 概念与 langchain4j 知识点：
     * 1. 声明式 AI 服务 (AI Services)：隐藏了底层复杂的 API 调用，通过简单接口即可与大模型交互。
     * 2. 静态系统提示词注解 (@SystemMessage)：直接在方法上固定死 AI 的角色和行为设定（即人设）。
     *
     * @param message 用户输入的需要翻译的英文单词或句子
     * @return AI 模型返回的翻译结果
     */
    @SystemMessage("你是一位专业的单词翻译专家")
    String chat(String message);
}
