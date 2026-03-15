package com.example.travelagent.service;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.ai.chat.messages.AssistantMessage;

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

    private TravelService travelService;

    @BeforeEach
    void setUp() {
        travelService = new TravelService(weatherAgent, travelAgent);
    }

    @Test
    @DisplayName("测试查询天气服务")
    void testQueryWeather() throws Exception {
        // Given
        String city = "杭州";
        String expectedResponse = "杭州今天天气晴朗，温度25°C";
        when(weatherAgent.call(anyString())).thenReturn(assistantMessage(expectedResponse));

        // When
        String result = travelService.queryWeather(city);

        // Then
        assertEquals(expectedResponse, result);
        verify(weatherAgent).call(contains("杭州"));
    }

    @Test
    @DisplayName("测试生成出行推荐服务")
    void testGenerateRecommendation() throws Exception {
        // Given
        String city = "北京";
        String weather = "晴朗";
        int temperature = 20;
        String expectedResponse = "推荐您乘坐地铁出行，参观故宫和天安门";
        when(travelAgent.call(anyString())).thenReturn(assistantMessage(expectedResponse));

        // When
        String result = travelService.generateRecommendation(city, weather, temperature);

        // Then
        assertEquals(expectedResponse, result);
        verify(travelAgent).call(argThat((String prompt) ->
                prompt.contains("北京") && prompt.contains("晴朗")));
    }

    @Test
    @DisplayName("测试完整旅行规划服务")
    void testPlanTravel() throws Exception {
        // Given
        String city = "上海";
        String weatherResponse = "上海天气多云";
        String recommendationResponse = "推荐游览外滩和东方明珠";
        when(weatherAgent.call(anyString())).thenReturn(assistantMessage(weatherResponse));
        when(travelAgent.call(anyString())).thenReturn(assistantMessage(recommendationResponse));

        // When
        String result = travelService.planTravel(city);

        // Then
        assertEquals("🌤️ 天气信息：\n上海天气多云\n\n🎯 出行推荐：\n推荐游览外滩和东方明珠", result);
        verify(weatherAgent).call(contains("上海"));
        verify(travelAgent).call(argThat((String prompt) ->
                prompt.contains("上海") && prompt.contains(weatherResponse)));
    }

    @Test
    @DisplayName("测试智能问答服务")
    void testChat() throws Exception {
        // Given
        String message = "我想去杭州旅游";
        String expectedResponse = "杭州是个很好的旅游目的地，我可以帮您查询天气和推荐行程";
        when(travelAgent.call(anyString())).thenReturn(assistantMessage(expectedResponse));

        // When
        String result = travelService.chat(message);

        // Then
        assertEquals(expectedResponse, result);
        verify(travelAgent).call(contains("杭州"));
    }

    @Test
    @DisplayName("测试服务异常处理")
    void testServiceException() throws Exception {
        // Given
        String city = "杭州";
        when(weatherAgent.call(anyString())).thenThrow(new RuntimeException("Agent 调用失败"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            travelService.queryWeather(city);
        });
    }

    private AssistantMessage assistantMessage(String text) {
        return new AssistantMessage(text);
    }
}
