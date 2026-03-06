package cn.ai.agent.easyaiagent.agent.serivce;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

/**
 * 基于springboot框架创建实例
 * <p>
 * <b>注意</b>
 * 如果AiService除了tool，其他某个属性有多个bean实例，不明确指定的情况下，需要把wiringMode设置为EXPLICIT
 * 因为AiServicesAutoConfig中的addBeanReference方法多个实例在不是EXPLICIT的情况下会报错
 * <p>
 * if (aiServiceAnnotation.wiringMode() == EXPLICIT) {
 * if (isNotNullOrBlank(customBeanName)) {
 * propertyValues.add(factoryPropertyName, new RuntimeBeanReference(customBeanName));
 * }
 * } else if (aiServiceAnnotation.wiringMode() == AUTOMATIC) {
 * if (beanNames.length == 1) {
 * propertyValues.add(factoryPropertyName, new RuntimeBeanReference(beanNames[0]));
 * } else if (beanNames.length > 1) {
 * throw conflict(beanType, beanNames, annotationAttributeName);
 * }
 * }
 *
 */
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT)
public interface AIServiceWithFrameworkAnnotationEnglishHelperService {

    /**
     * 该方法的具体作用：作为一个专业的单词翻译专家，接收用户输入的文本并通过读取外部系统提示词配置进行处理后返回翻译结果。
     * 涉及到的 AI 概念与 langchain4j 知识点：
     * 1. 声明式 AI 服务 Spring Boot 集成 (@AiService)：利用
     * langchain4j-spring-boot-starter，通过添加 @AiService 注解即可将接口自动注册为 Spring
     * Bean，无需再手动编写工厂类或配置类来创建动态代理服务，极大简化了开发流程。
     * 2. 外部化系统提示词加载
     * (@SystemMessage(fromResource))：配合声明式服务，直接从外部资源文件加载复杂人设设定，解耦代码与模型配置。
     *
     * @param message 用户输入的需要翻译的英文单词或句子
     * @return 经过 AI 模型按照预设 Prompt 处理后返回的结果
     */
    @SystemMessage(fromResource = "systemmessage/english_helper_system_message.txt")
    String chat(String message);
}
