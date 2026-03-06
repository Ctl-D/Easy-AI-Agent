package cn.ai.agent.easyaiagent.agent.serivce;

import cn.ai.agent.easyaiagent.dto.Animal;
import cn.ai.agent.easyaiagent.dto.Person;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

/**
 * 结构化输出
 */
@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatModel = "qwenChatModel")
public interface JsonSchemaService {

    /**
     * 该方法的具体作用：根据用户输入的文本内容，结合预设的系统提示词，提取关键信息并自动装配成对应的 Person 实体对象模型返回。
     * 涉及到的 AI 概念与 langchain4j 知识点：
     * 1. 结构化输出 (Structured Outputs / JSON Schema)：通过直接将方法的返回值定义为一个具象化的实体类（如 Record
     * 或 POJO），底层框架会自动干预大模型，要求其强制按照该对象的字段结构（JSON
     * Schema）输出结果，并自动完成反序列化转换，彻底解决了依靠文字正则提取信息的痛点。
     *
     * @param message 用户输入的自然语言文本
     * @return 经过 AI 模型解析并严格按照结构化要求生成的 Person 实例
     */
    @SystemMessage(fromResource = "systemmessage/person_schema_message.text")
    Person chatWithPerson(String message);

    /**
     * 该方法的具体作用：接收动物描述信息，提取动物的专业分类属性，并自动装配成 Animal 实体返回。
     * 涉及到的 AI 概念与 langchain4j 知识点：
     * 1. 字段级指令增强 (@Description)：在结构化输出的过程中，有时候仅仅靠字段的英文变量名无法让大模型准确理解提取要求（例如什么是
     * phylum）。通过在对象的类或字段上添加 @Description 注解，可以将额外的自然语言指令或示例直接嵌入到生成的 JSON Schema
     * 中，从而极大地提高大模型填充数据的准确率。
     * 
     * @param message 用户输入的动物相关描述文本
     * @return 经过 AI 模型利用增强 Schema 解析生成的 Animal 实例
     */
    @SystemMessage(fromResource = "systemmessage/animal_schema_message.txt")
    Animal chatWithAnimal(String message);
}
