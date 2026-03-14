package com.example.travelagent.config;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.example.travelagent.tool.TravelRecommendationTool;
import com.example.travelagent.tool.WeatherTool;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Agent 配置类
 * 
 * 配置两个 Agent：
 * 1. WeatherAgent - 天气查询 Agent
 * 2. TravelAgent - 出行推荐 Agent
 */
@Configuration
public class AgentConfig {
    
    /**
     * 天气查询 Agent
     * 
     * 职责：
     * 1. 接收城市名称
     * 2. 调用天气查询工具
     * 3. 返回结构化天气数据
     */
    @Bean
    public ReactAgent weatherAgent(ChatModel chatModel, WeatherTool weatherTool) {
        
        String instruction = """
                你是一个专业的天气查询助手。
                
                职责：
                1. 接收用户提供的城市名称
                2. 使用天气查询工具获取实时天气数据
                3. 以友好、清晰的方式向用户展示天气信息
                
                输出格式：
                - 城市名称
                - 当前温度
                - 天气状况
                - 湿度、风速等详细信息
                - 未来天气预报
                
                注意事项：
                - 如果用户没有指定城市，请礼貌询问
                - 如果查询失败，告知用户并提供建议
                """;
        
        return ReactAgent.builder()
                .name("WeatherAgent")
                .model(chatModel)
                .instruction(instruction)
                .enableLogging(true)
                .saver(new MemorySaver())
                .tools(
                        FunctionToolCallback.builder("query_weather", weatherTool)
                                .description("查询指定城市的天气信息，包括温度、天气状况、湿度、风速等")
                                .inputType(WeatherTool.WeatherRequest.class)
                                .build()
                )
                .build();
    }
    
    /**
     * 出行推荐 Agent
     * 
     * 职责：
     * 1. 接收天气数据
     * 2. 生成出行推荐
     * 3. 提供穿衣建议、景点推荐等
     */
    @Bean
    public ReactAgent travelAgent(ChatModel chatModel, TravelRecommendationTool travelTool) {
        
        String instruction = """
                你是一个专业的出行推荐助手。
                
                职责：
                1. 根据天气数据生成出行建议
                2. 推荐适合的出行方式
                3. 推荐适合的景点和活动
                4. 提供穿衣建议
                5. 提醒注意事项
                
                输出格式：
                - 出行方式推荐
                - 景点推荐
                - 穿衣建议
                - 注意事项
                
                风格：
                - 友好、贴心
                - 实用、具体
                - 考虑用户实际需求
                """;
        
        return ReactAgent.builder()
                .name("TravelAgent")
                .model(chatModel)
                .instruction(instruction)
                .enableLogging(true)
                .saver(new MemorySaver())
                .tools(
                        FunctionToolCallback.builder("generate_travel_recommendation", travelTool)
                                .description("根据城市和天气信息生成出行推荐，包括出行方式、景点、穿衣建议等")
                                .inputType(TravelRecommendationTool.TravelRequest.class)
                                .build()
                )
                .build();
    }
}
