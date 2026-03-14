package com.example.travelagent.tool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * WeatherTool 测试类
 * 
 * 测试天气查询工具的各项功能
 */
class WeatherToolTest {

    private WeatherTool weatherTool;

    @BeforeEach
    void setUp() {
        weatherTool = new WeatherTool();
    }

    @Test
    @DisplayName("测试查询杭州天气")
    void testQueryHangzhouWeather() {
        // Given
        WeatherTool.WeatherRequest request = new WeatherTool.WeatherRequest();
        request.setCity("杭州");
        request.setDate("today");

        // When
        WeatherTool.WeatherResponse response = weatherTool.apply(request);

        // Then
        assertNotNull(response);
        assertEquals("杭州", response.getCity());
        assertTrue(response.getTemperature() >= -50 && response.getTemperature() <= 50);
        assertNotNull(response.getCondition());
        assertNotNull(response.getQueryTime());
    }

    @Test
    @DisplayName("测试查询北京天气")
    void testQueryBeijingWeather() {
        // Given
        WeatherTool.WeatherRequest request = new WeatherTool.WeatherRequest();
        request.setCity("北京");

        // When
        WeatherTool.WeatherResponse response = weatherTool.apply(request);

        // Then
        assertNotNull(response);
        assertEquals("北京", response.getCity());
        assertNotNull(response.getForecast());
    }

    @Test
    @DisplayName("测试查询不存在城市 - 应返回降级数据")
    void testQueryNonExistentCity() {
        // Given
        WeatherTool.WeatherRequest request = new WeatherTool.WeatherRequest();
        request.setCity("不存在的城市XYZ123");

        // When
        WeatherTool.WeatherResponse response = weatherTool.apply(request);

        // Then - 即使城市不存在，也应该返回降级数据而不是抛出异常
        assertNotNull(response);
        assertNotNull(response.getCity());
        assertNotNull(response.getCondition());
    }

    @Test
    @DisplayName("测试天气数据结构完整性")
    void testWeatherResponseStructure() {
        // Given
        WeatherTool.WeatherRequest request = new WeatherTool.WeatherRequest();
        request.setCity("上海");

        // When
        WeatherTool.WeatherResponse response = weatherTool.apply(request);

        // Then
        assertAll("验证天气数据结构",
            () -> assertNotNull(response.getCity(), "城市不应为空"),
            () -> assertTrue(response.getTemperature() >= -100 && response.getTemperature() <= 100, "温度应在合理范围内"),
            () -> assertTrue(response.getHumidity() >= 0 && response.getHumidity() <= 100, "湿度应在0-100之间"),
            () -> assertTrue(response.getWindSpeed() >= 0, "风速不应为负"),
            () -> assertNotNull(response.getQueryTime(), "查询时间不应为空")
        );
    }
}
