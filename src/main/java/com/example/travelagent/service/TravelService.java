package com.example.travelagent.service;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.SupervisorAgent;

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
    private final SupervisorAgent travelSupervisor;
    
    /**
     * 查询天气
     */
    public String queryWeather(String city) {
        log.info("🌤️ Service - 查询天气: {}", city);
        
        String prompt = String.format("请查询 %s 的天气信息", city);
        
        // 调用 WeatherAgent
        return weatherAgent.run(prompt);
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
        return travelAgent.run(prompt);
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
        
        // 调用 SupervisorAgent 自动协调
        return travelSupervisor.run(prompt);
    }
    
    /**
     * 智能问答
     */
    public String chat(String message) {
        log.info("💬 Service - 智能问答: {}", message);
        
        // 调用 SupervisorAgent 自动路由
        return travelSupervisor.run(message);
    }
}
