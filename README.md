# 🤖 AI 旅行助手

基于 Spring AI Alibaba 的智能旅行 Agent 系统，支持自动查询各地天气和出行推荐。

## ✨ 功能特性

- 🌤️ **自动天气查询** - 查询指定城市的实时天气
- 🎯 **智能出行推荐** - 根据天气生成出行建议
- 🗺️ **完整旅行规划** - 一站式天气+推荐服务
- 💬 **智能问答** - 自然语言交互，Agent 自动路由
- 🤖 **多 Agent 协作** - WeatherAgent + TravelAgent + SupervisorAgent

## 🏗️ 架构设计

```
┌─────────────────────────────────────────────────────────┐
│                    TravelController                      │
│                    (REST API 层)                         │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                    TravelService                         │
│                    (业务逻辑层)                          │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                  SupervisorAgent                         │
│              (总协调员 - 自动路由)                        │
└──────────┬─────────────────────────┬────────────────────┘
           │                         │
┌──────────▼──────────┐   ┌──────────▼──────────┐
│    WeatherAgent     │   │    TravelAgent      │
│    (天气查询)        │   │    (出行推荐)        │
└──────────┬──────────┘   └──────────┬──────────┘
           │                         │
┌──────────▼──────────┐   ┌──────────▼──────────┐
│   WeatherTool       │   │ TravelRecommendationTool
│  (天气查询工具)      │   │  (出行推荐工具)       │
└─────────────────────┘   └─────────────────────┘
```

## 🚀 快速开始

### 1. 配置 API Key

在 `application.yml` 中配置 DashScope API Key：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-dashscope-api-key
```

或设置环境变量：

```bash
export AI_DASHSCOPE_API_KEY=your-api-key
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

### 3. 测试 API

#### 查询天气

```bash
curl -X POST http://localhost:8080/api/travel/weather \
  -H "Content-Type: application/json" \
  -d '{"city": "杭州"}'
```

#### 生成出行推荐

```bash
curl -X POST http://localhost:8080/api/travel/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "city": "杭州",
    "weatherCondition": "晴朗",
    "temperature": 25
  }'
```

#### 完整旅行规划

```bash
curl -X POST http://localhost:8080/api/travel/plan \
  -H "Content-Type: application/json" \
  -d '{"city": "杭州"}'
```

#### 智能问答

```bash
curl -X POST http://localhost:8080/api/travel/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "我想去杭州旅游，天气怎么样？"}'
```

## 📁 项目结构

```
ai-travel-agent/
├── pom.xml                                    # Maven 配置
├── README.md                                  # 项目文档
├── src/
│   ├── main/
│   │   ├── java/com/example/travelagent/
│   │   │   ├── TravelAgentApplication.java    # 应用入口
│   │   │   ├── config/
│   │   │   │   └── AgentConfig.java           # Agent 配置
│   │   │   ├── controller/
│   │   │   │   └── TravelController.java      # REST API
│   │   │   ├── service/
│   │   │   │   └── TravelService.java         # 业务逻辑
│   │   │   └── tool/
│   │   │       ├── WeatherTool.java           # 天气查询工具
│   │   │       └── TravelRecommendationTool.java  # 出行推荐工具
│   │   └── resources/
│   │       └── application.yml                # 应用配置
│   └── test/                                  # 测试代码
```

## 🔧 核心组件

### Agent 类型

| Agent | 职责 | 说明 |
|-------|------|------|
| **WeatherAgent** | 天气查询 | 接收城市名称，返回天气数据 |
| **TravelAgent** | 出行推荐 | 根据天气生成出行建议 |
| **TravelPlanningAgent** | 旅行规划 | 顺序执行（天气→推荐）|
| **TravelSupervisor** | 总协调 | 智能路由用户请求 |

### 工具类

| 工具 | 功能 |
|------|------|
| **WeatherTool** | 查询城市天气（使用 wttr.in API）|
| **TravelRecommendationTool** | 生成出行推荐、穿衣建议、景点推荐 |

## 🛠️ 技术栈

- **Java 17**
- **Spring Boot 3.2**
- **Spring AI Alibaba 1.1.2.0**
- **DashScope（阿里云百炼）**
- **WebFlux**（响应式编程）
- **Lombok**

## 📚 参考文档

- [Spring AI Alibaba 文档](https://java2ai.com/docs/)
- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)

## 📝 License

Apache License 2.0
