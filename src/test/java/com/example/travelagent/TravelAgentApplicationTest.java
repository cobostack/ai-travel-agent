package com.example.travelagent;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * 应用上下文测试
 * 
 * 验证 Spring Boot 应用能正常启动
 */
@SpringBootTest
class TravelAgentApplicationTest {

    @MockBean
    private ChatModel chatModel;

    @Test
    void contextLoads() {
        // 验证 Spring 上下文能正常加载
    }
}
