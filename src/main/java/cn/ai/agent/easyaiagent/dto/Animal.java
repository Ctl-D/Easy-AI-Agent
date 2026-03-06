package cn.ai.agent.easyaiagent.dto;

import jdk.jfr.Description;

/**
 * 如果 LLM 没有提供所需的输出，可以使用 @Description 注解类和字段， 为 LLM 提供更多指令和正确输出的示例
 * @param name
 * @param phylum
 * @param animalClass
 * @param order
 * @param family
 * @param genus
 * @param species
 * @param conservationStatus
 */
@Description("动物信息")
public record Animal(
        @Description("动物通用名称，如：东北虎、非洲狮")
        String name,
        @Description("门：生物分类的主要门类，如：脊索动物门")
        String phylum,
        @Description("纲：如哺乳纲")
        String animalClass,
        @Description("目：如食肉目")
        String order,
        @Description("科：如猫科")
        String family,
        @Description("属：如豹属")
        String genus,
        @Description("种：如虎、狮")
        String species,
        @Description("保护等级：极危CR、濒危EN、易危VU、无危LC等")
        String conservationStatus
) {
}
