package com.example.travelagent.controller;

import com.example.travelagent.service.TravelService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TravelController 测试类
 * 
 * 测试 REST API 接口
 */
@WebMvcTest(TravelController.class)
class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TravelService travelService;

    @Test
    @DisplayName("测试查询天气接口")
    void testQueryWeather() throws Exception {
        // Given
        String city = "杭州";
        String expectedResponse = "杭州今天天气晴朗";
        when(travelService.queryWeather(city)).thenReturn(expectedResponse);

        TravelController.WeatherRequest request = new TravelController.WeatherRequest();
        request.setCity(city);

        // When & Then
        mockMvc.perform(post("/api/travel/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(expectedResponse));

        verify(travelService).queryWeather(city);
    }

    @Test
    @DisplayName("测试生成出行推荐接口")
    void testGenerateRecommendation() throws Exception {
        // Given
        String expectedResponse = "推荐您乘坐地铁出行";
        when(travelService.generateRecommendation(anyString(), anyString(), anyInt()))
                .thenReturn(expectedResponse);

        TravelController.RecommendRequest request = new TravelController.RecommendRequest();
        request.setCity("北京");
        request.setWeatherCondition("晴朗");
        request.setTemperature(25);

        // When & Then
        mockMvc.perform(post("/api/travel/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(expectedResponse));
    }

    @Test
    @DisplayName("测试完整旅行规划接口")
    void testPlanTravel() throws Exception {
        // Given
        String expectedResponse = "上海旅行规划完成";
        when(travelService.planTravel(anyString())).thenReturn(expectedResponse);

        TravelController.TravelPlanRequest request = new TravelController.TravelPlanRequest();
        request.setCity("上海");

        // When & Then
        mockMvc.perform(post("/api/travel/plan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(expectedResponse));
    }

    @Test
    @DisplayName("测试智能问答接口")
    void testChat() throws Exception {
        // Given
        String expectedResponse = "我可以帮您规划杭州之旅";
        when(travelService.chat(anyString())).thenReturn(expectedResponse);

        TravelController.ChatRequest request = new TravelController.ChatRequest();
        request.setMessage("我想去杭州旅游");

        // When & Then
        mockMvc.perform(post("/api/travel/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(expectedResponse));
    }

    @Test
    @DisplayName("测试健康检查接口")
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/travel/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("AI 旅行助手运行正常 🌤️"));
    }

    @Test
    @DisplayName("测试接口异常情况")
    void testServiceException() throws Exception {
        // Given
        when(travelService.queryWeather(anyString()))
                .thenThrow(new RuntimeException("服务异常"));

        TravelController.WeatherRequest request = new TravelController.WeatherRequest();
        request.setCity("杭州");

        // When & Then
        mockMvc.perform(post("/api/travel/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("天气查询失败: 服务异常"));
    }
}
