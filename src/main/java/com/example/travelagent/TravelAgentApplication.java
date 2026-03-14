package com.example.travelagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI 旅行助手应用入口
 * 
 * 功能：
 * 1. 自动查询各地天气
 * 2. 智能出行推荐
 * 3. 多 Agent 协作（天气查询 Agent + 出行推荐 Agent）
 */
@SpringBootApplication
public class TravelAgentApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TravelAgentApplication.class, args);
        System.out.println("\n🌤️ ======================================== 🌤️");
        System.out.println("✅ AI 旅行助手已启动！");
        System.out.println("🚀 API 地址: http://localhost:8080/api/travel");
        System.out.println("📖 文档地址: http://localhost:8080/swagger-ui.html");
        System.out.println("🌤️ ======================================== 🌤️\n");
    }
}
