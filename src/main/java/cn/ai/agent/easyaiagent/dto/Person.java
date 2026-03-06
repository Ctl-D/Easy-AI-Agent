package cn.ai.agent.easyaiagent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 结构化输出对象，定义方式为record
 * 要使字段成为必需的，可以使用 @JsonProperty(required = true)
 *
 * @param age
 * @param name
 * @param country
 * @param birth
 */
public record Person(Integer age,
                     @JsonProperty(required = true) String name,
                     String country,
                     String birth) {

}
