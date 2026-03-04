package cn.ai.agent.easyaiagent.agenttest;

import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithFrameworkAnnotationEnglishHelperService;
import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationEnglishHelperService;
import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationResourceEnglishHelperService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AIServiceTest {

    @Resource
    private AIServiceWithAnnotationEnglishHelperService annotationEnglishHelperService;

    @Resource
    private AIServiceWithAnnotationResourceEnglishHelperService annotationResourceEnglishHelperService;

    @Resource
    private AIServiceWithFrameworkAnnotationEnglishHelperService frameworkAnnotationEnglishHelperService;

    @Test
    public void annotationTest() {
        String message = annotationEnglishHelperService.chat("charge");
        System.out.println(message);
    }

    @Test
    public void annotationResourceTest() {
        String message = annotationResourceEnglishHelperService.chat("salary");
        System.out.println(message);
    }

    @Test
    public void frameworkAnnotationTest() {
        String message = frameworkAnnotationEnglishHelperService.chat("charge");
        System.out.println(message);
    }
}
