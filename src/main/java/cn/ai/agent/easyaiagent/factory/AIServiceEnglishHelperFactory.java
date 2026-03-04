package cn.ai.agent.easyaiagent.factory;

import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationEnglishHelperService;
import cn.ai.agent.easyaiagent.agent.serivce.AIServiceWithAnnotationResourceEnglishHelperService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于AI Service创建实例
 */
@Configuration
public class AIServiceEnglishHelperFactory {

    @Resource
    private ChatModel chatModel;

    @Bean
    public AIServiceWithAnnotationEnglishHelperService aiServiceEnglishHelperService() {
        return AiServices.create(AIServiceWithAnnotationEnglishHelperService.class, chatModel);
    }

    @Bean
    public AIServiceWithAnnotationResourceEnglishHelperService aiServiceWithAnnotationResourceEnglishHelperService() {
        return AiServices.create(AIServiceWithAnnotationResourceEnglishHelperService.class, chatModel);
    }
}
