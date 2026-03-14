package com.example.travelagent.service;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.SupervisorAgent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TravelService 测试类
 * 
 * 测试业务逻辑层的各项功能
 */
@ExtendWith(MockitoExtension.class)
class TravelServiceTest {

    @Mock
    private ReactAgent weatherAgent;

    @Mock
    private ReactAgent travelAgent;

    @Mock
    private SupervisorAgent travelSupervisor;

    private TravelService travelService;

    @BeforeEach
    void setUp() {
        travelService = new TravelService(weatherAgent, travelAgent, travelSupervisor);
    }

    @Test
    @DisplayName("测试查询天气服务")
    void testQueryWeather() {
        // Given
        String city = "杭州";
        String expectedResponse = "杭州今天天气晴朗，温度25°C";
        when(weatherAgent.run(anyString())).thenReturn(expectedResponse);

        // When
        String result = travelService.queryWeather(city);

        // Then
        assertEquals(expectedResponse, result);
        verify(weatherAgent).run(contains("杭州"));
    }

    @Test
    @DisplayName("测试生成出行推荐服务")
    void testGenerateRecommendation() {
        // Given
        String city = "北京";
        String weather = "晴朗";
        int temperature = 20;
        String expectedResponse = "推荐您乘坐地铁出行，参观故宫和天安门";
        when(travelAgent.run(anyString())).thenReturn(expectedResponse);

        // When
        String result = travelService.generateRecommendation(city, weather, temperature);

        // Then
        assertEquals(expectedResponse, result);
        verify(travelAgent).run(contains("北京"));
        verify(travelAgent).run(contains("晴朗"));
    }

    @Test
    @DisplayName("测试完整旅行规划服务")
    void testPlanTravel() {
        // Given
        String city = "上海";
        String expectedResponse = "上海旅行规划：天气多云，推荐外滩和东方明珠";
        when(travelSupervisor.run(anyString())).thenReturn(expectedResponse);

        // When
        String result = travelService.planTravel(city);

        // Then
        assertEquals(expectedResponse, result);
        verify(travelSupervisor).run(contains("上海"));
    }

    @Test
    @DisplayName("测试智能问答服务")
    void testChat() {
        // Given
        String message = "我想去杭州旅游";
        String expectedResponse = "杭州是个很好的旅游目的地，我可以帮您查询天气和推荐行程";
        when(travelSupervisor.run(anyString())).thenReturn(expectedResponse);

        // When
        String result = travelService.chat(message);

        // Then
        assertEquals(expectedResponse, result);
        verify(travelSupervisor).run(contains("杭州"));
    }

    @Test
    @DisplayName("测试服务异常处理")
    void testServiceException() {
        // Given
        String city = "杭州";
        when(weatherAgent.run(anyString())).thenThrow(new RuntimeException("Agent 调用失败"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            travelService.queryWeather(city);
        });
    }
}
