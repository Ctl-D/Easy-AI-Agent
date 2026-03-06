package cn.ai.agent.easyaiagent.agenttest;

import cn.ai.agent.easyaiagent.agent.JsonSchemaAgent;
import cn.ai.agent.easyaiagent.agent.serivce.JsonSchemaService;
import cn.ai.agent.easyaiagent.dto.Animal;
import cn.ai.agent.easyaiagent.dto.Person;
import com.alibaba.dashscope.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JsonSchemaTest {

    @Resource
    private JsonSchemaService jsonSchemaService;

    @Resource
    private JsonSchemaAgent agent;
    @Test
    public void originMethodTest() {
        String chat = agent.chat("乔布斯的基础信息");
        System.out.println(chat);
    }
    @Test
    public void personTest() {

        Person person = jsonSchemaService.chatWithPerson("乔布斯的基础信息");
        System.out.println(JsonUtils.toJson(person));
    }

    @Test
    public void animalTest() {
        Animal animal = jsonSchemaService.chatWithAnimal("介绍下土拨鼠");
        System.out.println(JsonUtils.toJson(animal));
    }
}
