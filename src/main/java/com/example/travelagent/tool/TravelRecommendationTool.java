package com.example.travelagent.tool;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 出行推荐工具
 * 
 * 功能：
 * 1. 根据天气推荐出行方式
 * 2. 推荐景点和活动
 * 3. 提供穿衣建议
 */
@Slf4j
@Component
public class TravelRecommendationTool implements Function<TravelRecommendationTool.TravelRequest, TravelRecommendationTool.TravelResponse> {
    
    @Override
    public TravelResponse apply(TravelRequest request) {
        log.info("🎯 生成出行推荐: 城市={}, 天气={}", request.getCity(), request.getWeatherCondition());
        
        TravelResponse response = new TravelResponse();
        response.setCity(request.getCity());
        
        // 根据天气生成推荐
        String weather = request.getWeatherCondition();
        int temperature = request.getTemperature();
        
        // 出行方式推荐
        response.setTransportationRecommend(generateTransportationRecommendation(weather, temperature));
        
        // 景点推荐
        response.setAttractionRecommend(generateAttractionRecommendation(request.getCity(), weather));
        
        // 穿衣建议
        response.setClothingAdvice(generateClothingAdvice(temperature, weather));
        
        // 注意事项
        response.setPrecautions(generatePrecautions(weather));
        
        log.info("✅ 出行推荐生成完成");
        return response;
    }
    
    /**
     * 生成出行方式推荐
     */
    private String generateTransportationRecommendation(String weather, int temperature) {
        StringBuilder sb = new StringBuilder();
        
        if (weather.contains("雨") || weather.contains("Rain")) {
            sb.append("🚗 推荐私家车或打车出行，避免淋湿\n");
            sb.append("🚇 地铁是雨天最可靠的交通方式\n");
            sb.append("🚌 公交也是不错的选择，注意提前出门");
        } else if (weather.contains("晴") || weather.contains("Clear")) {
            sb.append("🚴 天气晴朗，推荐骑行或步行，享受阳光\n");
            sb.append("🚗 自驾出行也很舒适\n");
            sb.append("🚇 公共交通同样方便");
        } else if (temperature > 35) {
            sb.append("🚗 天气炎热，推荐空调车出行\n");
            sb.append("🚇 地铁有空调，是最佳选择\n");
            sb.append("⏰ 避开中午高温时段出行");
        } else {
            sb.append("🚗 各种出行方式均可\n");
            sb.append("🚇 公共交通经济实惠\n");
            sb.append("🚴 短途推荐步行或骑行");
        }
        
        return sb.toString();
    }
    
    /**
     * 生成景点推荐
     */
    private String generateAttractionRecommendation(String city, String weather) {
        StringBuilder sb = new StringBuilder();
        
        // 根据城市推荐（简化版，实际可对接景点 API）
        sb.append("📍 ").append(city).append("推荐景点：\n\n");
        
        if (weather.contains("雨") || weather.contains("Rain")) {
            sb.append("🏛️ 博物馆、美术馆（室内避雨）\n");
            sb.append("🛍️ 购物中心、商场\n");
            sb.append("☕ 咖啡厅、书店\n");
            sb.append("🎬 电影院、剧院");
        } else if (weather.contains("晴") || weather.contains("Clear")) {
            sb.append("🌳 城市公园、植物园\n");
            sb.append("🏛️ 历史古迹、文化景点\n");
            sb.append("🎡 游乐园、主题公园\n");
            sb.append("🏖️ 如果临海，推荐海边游玩");
        } else {
            sb.append("🌳 公园、自然景点\n");
            sb.append("🏛️ 博物馆、文化场所\n");
            sb.append("🛍️ 商业街区");
        }
        
        return sb.toString();
    }
    
    /**
     * 生成穿衣建议
     */
    private String generateClothingAdvice(int temperature, String weather) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("👔 穿衣建议：\n\n");
        
        if (temperature < 10) {
            sb.append("🧥 羽绒服或厚外套\n");
            sb.append("🧣 围巾、手套必备\n");
            sb.append("👢 保暖鞋子");
        } else if (temperature < 20) {
            sb.append("🧥 薄外套或风衣\n");
            sb.append("👔 长袖衬衫\n");
            sb.append("👖 长裤");
        } else if (temperature < 30) {
            sb.append("👕 短袖T恤或薄衬衫\n");
            sb.append("👖 长裤或薄裤\n");
            sb.append("🧥 备一件薄外套（空调房用）");
        } else {
            sb.append("👕 轻薄透气的短袖\n");
            sb.append("🩳 短裤或薄裙\n");
            sb.append("🧢 帽子防晒\n");
            sb.append("🕶️ 太阳镜");
        }
        
        // 根据天气添加建议
        if (weather.contains("雨") || weather.contains("Rain")) {
            sb.append("\n☔ 记得带雨伞或雨衣");
        } else if (weather.contains("晴") || weather.contains("Clear")) {
            sb.append("\n🧴 涂抹防晒霜");
        }
        
        return sb.toString();
    }
    
    /**
     * 生成注意事项
     */
    private String generatePrecautions(String weather) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("⚠️ 出行注意事项：\n\n");
        
        if (weather.contains("雨") || weather.contains("Rain")) {
            sb.append("• 路面湿滑，注意交通安全\n");
            sb.append("• 随身携带雨具\n");
            sb.append("• 避免前往低洼易积水地区");
        } else if (weather.contains("晴") || weather.contains("Clear")) {
            sb.append("• 注意防晒，多喝水\n");
            sb.append("• 避免长时间暴晒\n");
            sb.append("• 中午时段尽量在室内活动");
        } else if (weather.contains("雪") || weather.contains("Snow")) {
            sb.append("• 注意防寒保暖\n");
            sb.append("• 道路可能结冰，小心行走\n");
            sb.append("• 交通可能受影响，提前规划");
        } else {
            sb.append("• 关注天气变化\n");
            sb.append("• 随身携带必要物品\n");
            sb.append("• 保持手机电量充足");
        }
        
        return sb.toString();
    }
    
    // ============ 请求/响应类 ============
    
    @Data
    public static class TravelRequest {
        private String city;              // 城市
        private String weatherCondition;  // 天气状况
        private int temperature;          // 温度
    }
    
    @Data
    public static class TravelResponse {
        private String city;                      // 城市
        private String transportationRecommend;   // 出行方式推荐
        private String attractionRecommend;       // 景点推荐
        private String clothingAdvice;            // 穿衣建议
        private String precautions;               // 注意事项
    }
}
