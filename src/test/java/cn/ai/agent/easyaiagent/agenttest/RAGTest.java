package cn.ai.agent.easyaiagent.agenttest;

import cn.ai.agent.easyaiagent.agent.AdvancedRAGAgent;
import cn.ai.agent.easyaiagent.agent.EasyRAGAgent;
import cn.ai.agent.easyaiagent.agent.StandardRAGAgent;
import com.alibaba.dashscope.utils.JsonUtils;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RAGTest {

    @Resource
    private EasyRAGAgent easyRAGAgent;

    @Resource
    private StandardRAGAgent standardRAGAgent;

    @Resource
    private AdvancedRAGAgent advancedRAGAgent;

    @Test
    public void easyTest() {
        System.out.println(easyRAGAgent.chat("小明喜欢什么？"));

        System.out.println(easyRAGAgent.chat("小明住在哪？"));

        System.out.println(easyRAGAgent.chat("小芳喜欢什么？"));

        System.out.println(easyRAGAgent.chat("小芳知道小明喜欢她吗？"));
    }

    @Test
    public void standardTest() {
        System.out.println(standardRAGAgent.chat("小明是什么人？有喜欢的人吗？喜欢的人知道小明喜欢她吗？"));
    }

    @Test
    public void standardWithResultSourceTest() {
        Result<String> result = standardRAGAgent.chatWithResult("小明是什么人？有喜欢的人吗？喜欢的人知道小明喜欢她吗？");
        System.out.println(result.content());
        List<Content> sources = result.sources();
        System.out.println(JsonUtils.toJson(sources));
    }

    @Test
    public void advancedTest() {
        System.out.println(advancedRAGAgent.chat("张明远在哪工作？多少岁了？和王建国什么关系？有没有喜欢的人？"));
    }
}
