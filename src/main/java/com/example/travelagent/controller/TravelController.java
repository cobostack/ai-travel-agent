package com.example.travelagent.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.SupervisorAgent;
import com.example.travelagent.service.TravelService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 旅行助手 API 控制器
 * 
 * 提供 REST API 接口：
 * 1. 查询天气
 * 2. 生成出行推荐
 * 3. 完整旅行规划
 */
@Slf4j
@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
public class TravelController {
    
    private final TravelService travelService;
    private final ReactAgent weatherAgent;
    private final ReactAgent travelAgent;
    private final SupervisorAgent travelSupervisor;
    
    /**
     * 查询天气
     * 
     * POST /api/travel/weather
     * Body: { "city": "杭州" }
     */
    @PostMapping("/weather")
    public ApiResponse<String> queryWeather(@RequestBody WeatherRequest request) {
        log.info("🌤️ 查询天气: city={}", request.getCity());
        
        try {
            String result = travelService.queryWeather(request.getCity());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("❌ 天气查询失败: {}", e.getMessage());
            return ApiResponse.error("天气查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成出行推荐
     * 
     * POST /api/travel/recommend
     * Body: { "city": "杭州", "weatherCondition": "晴朗", "temperature": 25 }
     */
    @PostMapping("/recommend")
    public ApiResponse<String> generateRecommendation(@RequestBody RecommendRequest request) {
        log.info("🎯 生成出行推荐: city={}, weather={}", 
                request.getCity(), request.getWeatherCondition());
        
        try {
            String result = travelService.generateRecommendation(
                    request.getCity(),
                    request.getWeatherCondition(),
                    request.getTemperature()
            );
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("❌ 出行推荐生成失败: {}", e.getMessage());
            return ApiResponse.error("出行推荐生成失败: " + e.getMessage());
        }
    }
    
    /**
     * 完整旅行规划
     * 
     * POST /api/travel/plan
     * Body: { "city": "杭州" }
     */
    @PostMapping("/plan")
    public ApiResponse<String> planTravel(@RequestBody TravelPlanRequest request) {
        log.info("🗺️ 旅行规划: city={}", request.getCity());
        
        try {
            String result = travelService.planTravel(request.getCity());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("❌ 旅行规划失败: {}", e.getMessage());
            return ApiResponse.error("旅行规划失败: " + e.getMessage());
        }
    }
    
    /**
     * 智能问答（Agent 自动路由）
     * 
     * POST /api/travel/chat
     * Body: { "message": "我想去杭州旅游，天气怎么样？" }
     */
    @PostMapping("/chat")
    public ApiResponse<String> chat(@RequestBody ChatRequest request) {
        log.info("💬 智能问答: message={}", request.getMessage());
        
        try {
            String result = travelService.chat(request.getMessage());
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("❌ 智能问答失败: {}", e.getMessage());
            return ApiResponse.error("智能问答失败: " + e.getMessage());
        }
    }
    
    /**
     * 健康检查
     * 
     * GET /api/travel/health
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("AI 旅行助手运行正常 🌤️");
    }
    
    // ============ 请求/响应类 ============
    
    @Data
    public static class WeatherRequest {
        private String city;
    }
    
    @Data
    public static class RecommendRequest {
        private String city;
        private String weatherCondition;
        private int temperature;
    }
    
    @Data
    public static class TravelPlanRequest {
        private String city;
    }
    
    @Data
    public static class ChatRequest {
        private String message;
    }
    
    @Data
    public static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;
        private long timestamp;
        
        public static <T> ApiResponse<T> success(T data) {
            ApiResponse<T> response = new ApiResponse<>();
            response.code = 200;
            response.message = "success";
            response.data = data;
            response.timestamp = System.currentTimeMillis();
            return response;
        }
        
        public static <T> ApiResponse<T> error(String message) {
            ApiResponse<T> response = new ApiResponse<>();
            response.code = 500;
            response.message = message;
            response.timestamp = System.currentTimeMillis();
            return response;
        }
    }
}
