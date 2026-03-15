package com.example.travelagent.service;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 旅行服务
 * 
 * 封装 Agent 调用逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {
    
    private final ReactAgent weatherAgent;
    private final ReactAgent travelAgent;
    
    /**
     * 查询天气
     */
    public String queryWeather(String city) {
        log.info("🌤️ Service - 查询天气: {}", city);
        
        String prompt = String.format("请查询 %s 的天气信息", city);
        
        // 调用 WeatherAgent
        return callAgent(weatherAgent, prompt);
    }
    
    /**
     * 生成出行推荐
     */
    public String generateRecommendation(String city, String weatherCondition, int temperature) {
        log.info("🎯 Service - 生成出行推荐: {}, {}, {}", city, weatherCondition, temperature);
        
        String prompt = String.format(
                "请为 %s 生成出行推荐。天气状况：%s，温度：%d°C",
                city, weatherCondition, temperature
        );
        
        // 调用 TravelAgent
        return callAgent(travelAgent, prompt);
    }
    
    /**
     * 完整旅行规划
     */
    public String planTravel(String city) {
        log.info("🗺️ Service - 旅行规划: {}", city);
        
        String prompt = String.format(
                "请为 %s 制定完整的旅行规划，包括天气查询和出行推荐",
                city
        );
        
        // 先查询天气，再生成推荐
        String weather = callAgent(weatherAgent, String.format("请查询 %s 的天气", city));
        String recommendation = callAgent(travelAgent, String.format("基于以下天气信息，为 %s 生成出行推荐：%s", city, weather));
        
        return String.format("🌤️ 天气信息：\n%s\n\n🎯 出行推荐：\n%s", weather, recommendation);
    }
    
    /**
     * 智能问答
     */
    public String chat(String message) {
        log.info("💬 Service - 智能问答: {}", message);
        
        // 根据消息内容决定调用哪个 Agent
        if (message.contains("天气") || message.contains("温度")) {
            return callAgent(weatherAgent, message);
        } else {
            return callAgent(travelAgent, message);
        }
    }

    private String callAgent(ReactAgent agent, String prompt) {
        try {
            return agent.call(prompt).getText();
        } catch (GraphRunnerException e) {
            throw new RuntimeException("Agent 调用失败", e);
        }
    }
}
