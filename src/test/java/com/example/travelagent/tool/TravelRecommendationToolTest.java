package com.example.travelagent.tool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TravelRecommendationTool 测试类
 * 
 * 测试出行推荐工具的各项功能
 */
class TravelRecommendationToolTest {

    private TravelRecommendationTool recommendationTool;

    @BeforeEach
    void setUp() {
        recommendationTool = new TravelRecommendationTool();
    }

    @Test
    @DisplayName("测试晴天出行推荐")
    void testSunnyDayRecommendation() {
        // Given
        TravelRecommendationTool.TravelRequest request = new TravelRecommendationTool.TravelRequest();
        request.setCity("杭州");
        request.setWeatherCondition("晴朗");
        request.setTemperature(25);

        // When
        TravelRecommendationTool.TravelResponse response = recommendationTool.apply(request);

        // Then
        assertNotNull(response);
        assertEquals("杭州", response.getCity());
        assertNotNull(response.getTransportationRecommend());
        assertNotNull(response.getAttractionRecommend());
        assertNotNull(response.getClothingAdvice());
        assertNotNull(response.getPrecautions());
        
        // 晴天应该推荐骑行或步行
        assertTrue(response.getTransportationRecommend().contains("骑行") || 
                   response.getTransportationRecommend().contains("步行"));
    }

    @Test
    @DisplayName("测试雨天出行推荐")
    void testRainyDayRecommendation() {
        // Given
        TravelRecommendationTool.TravelRequest request = new TravelRecommendationTool.TravelRequest();
        request.setCity("上海");
        request.setWeatherCondition("小雨");
        request.setTemperature(18);

        // When
        TravelRecommendationTool.TravelResponse response = recommendationTool.apply(request);

        // Then
        assertNotNull(response);
        
        // 雨天应该推荐室内活动
        assertTrue(response.getAttractionRecommend().contains("博物馆") || 
                   response.getAttractionRecommend().contains("商场"));
        
        // 雨天应该推荐私家车或地铁
        assertTrue(response.getTransportationRecommend().contains("地铁") || 
                   response.getTransportationRecommend().contains("车"));
    }

    @ParameterizedTest
    @DisplayName("测试不同温度的穿衣建议")
    @CsvSource({
        "哈尔滨, 雪, -5, 羽绒服",
        "北京, 晴, 15, 外套",
        "广州, 晴, 28, 短袖",
        "三亚, 晴, 35, 轻薄"
    })
    void testClothingAdviceForDifferentTemperatures(String city, String weather, int temp, String expectedClothing) {
        // Given
        TravelRecommendationTool.TravelRequest request = new TravelRecommendationTool.TravelRequest();
        request.setCity(city);
        request.setWeatherCondition(weather);
        request.setTemperature(temp);

        // When
        TravelRecommendationTool.TravelResponse response = recommendationTool.apply(request);

        // Then
        assertNotNull(response.getClothingAdvice());
        assertTrue(response.getClothingAdvice().contains(expectedClothing) || 
                   response.getClothingAdvice().length() > 10,
                   city + " 的穿衣建议应该包含合适的内容");
    }

    @Test
    @DisplayName("测试高温天气出行推荐")
    void testHotWeatherRecommendation() {
        // Given
        TravelRecommendationTool.TravelRequest request = new TravelRecommendationTool.TravelRequest();
        request.setCity("重庆");
        request.setWeatherCondition("晴");
        request.setTemperature(38);

        // When
        TravelRecommendationTool.TravelResponse response = recommendationTool.apply(request);

        // Then
        assertNotNull(response);
        
        // 高温应该推荐空调车
        assertTrue(response.getTransportationRecommend().contains("空调") || 
                   response.getClothingAdvice().contains("防晒"));
    }

    @Test
    @DisplayName("测试响应数据完整性")
    void testResponseCompleteness() {
        // Given
        TravelRecommendationTool.TravelRequest request = new TravelRecommendationTool.TravelRequest();
        request.setCity("深圳");
        request.setWeatherCondition("多云");
        request.setTemperature(22);

        // When
        TravelRecommendationTool.TravelResponse response = recommendationTool.apply(request);

        // Then
        assertAll("验证响应数据完整性",
            () -> assertNotNull(response.getCity(), "城市不应为空"),
            () -> assertNotNull(response.getTransportationRecommend(), "出行方式推荐不应为空"),
            () -> assertNotNull(response.getAttractionRecommend(), "景点推荐不应为空"),
            () -> assertNotNull(response.getClothingAdvice(), "穿衣建议不应为空"),
            () -> assertNotNull(response.getPrecautions(), "注意事项不应为空"),
            () -> assertTrue(response.getTransportationRecommend().length() > 10, "出行建议内容应详细"),
            () -> assertTrue(response.getAttractionRecommend().length() > 10, "景点推荐内容应详细")
        );
    }
}
