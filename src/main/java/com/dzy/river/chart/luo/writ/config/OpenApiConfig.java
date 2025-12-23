package com.dzy.river.chart.luo.writ.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * OpenAPI配置类
 * 用于配置Swagger文档信息和自动生成JSON文件
 */
@Slf4j
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${springdoc.api-docs.path:/v3/api-docs}")
    private String apiDocsPath;

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerUiPath;

    @Value("${openapi.json.output-dir:${user.dir}/docs}")
    private String outputDir;

    @Value("${openapi.json.file-name:river_chart_luo_writ.json}")
    private String fileName;

    @Value("${openapi.json.auto-generate:true}")
    private boolean autoGenerate;

    /**
     * 自定义OpenAPI信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("River Chart Luo Writ API")
                        .description("河图洛书内容管理系统 API 文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }

    /**
     * 应用启动后自动生成OpenAPI JSON文件
     */
    @Bean
    public ApplicationRunner generateOpenApiJson() {
        return args -> {
            if (!autoGenerate) {
                log.info("OpenAPI JSON自动生成已禁用");
                return;
            }

            // 等待应用完全启动
            Thread.sleep(3000);

            try {
                log.info("=".repeat(80));
                log.info("📚 OpenAPI 文档服务已启动");
                log.info("=".repeat(80));

                // 打印Swagger UI访问地址
                String swaggerUiUrl = String.format("http://localhost:%d%s", serverPort, swaggerUiPath);
                log.info("🌐 Swagger UI 地址: {}", swaggerUiUrl);

                // 构建API文档访问URL
                String apiUrl = String.format("http://localhost:%d%s", serverPort, apiDocsPath);
                log.info("📄 API 文档JSON: {}", apiUrl);
                log.info("=".repeat(80));

                log.info("开始生成OpenAPI JSON文件...");

                // 使用RestTemplate获取JSON内容
                RestTemplate restTemplate = new RestTemplate();
                String jsonContent = restTemplate.getForObject(apiUrl, String.class);

                if (jsonContent == null || jsonContent.isEmpty()) {
                    log.error("获取到的API文档内容为空");
                    return;
                }

                // 格式化JSON（美化）
                ObjectMapper objectMapper = new ObjectMapper();
                Object jsonObject = objectMapper.readValue(jsonContent, Object.class);
                String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);

                // 创建输出目录
                Path outputPath = Paths.get(outputDir);
                if (!Files.exists(outputPath)) {
                    Files.createDirectories(outputPath);
                    log.info("创建输出目录: {}", outputPath.toAbsolutePath());
                }

                // 写入文件
                Path filePath = outputPath.resolve(fileName);
                Files.writeString(filePath, prettyJson);

                log.info("=".repeat(80));
                log.info("✅ OpenAPI JSON文件生成成功!");
                log.info("📄 文件完整路径: {}", filePath.toAbsolutePath());
                log.info("📊 文件大小: {} KB", Files.size(filePath) / 1024);
                log.info("💡 提示: 可将此文件提供给前端团队作为API参考");
                log.info("=".repeat(80));

            } catch (Exception e) {
                log.error("生成OpenAPI JSON文件失败: {}", e.getMessage(), e);
            }
        };
    }

    /**
     * 分组API配置（基于实际Controller路径）
     */
    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("全部接口")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户管理")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("分类管理")
                .pathsToMatch("/main-category/**", "/sub-category/**")
                .build();
    }

    @Bean
    public GroupedOpenApi contentApi() {
        return GroupedOpenApi.builder()
                .group("内容管理")
                .pathsToMatch("/content/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tagApi() {
        return GroupedOpenApi.builder()
                .group("标签管理")
                .pathsToMatch("/tag/**", "/*-tag/**")
                .build();
    }

    @Bean
    public GroupedOpenApi collectionApi() {
        return GroupedOpenApi.builder()
                .group("收藏管理")
                .pathsToMatch("/user-collection/**")
                .build();
    }

    @Bean
    public GroupedOpenApi operationLogApi() {
        return GroupedOpenApi.builder()
                .group("操作日志")
                .pathsToMatch("/operation-log/**")
                .build();
    }
}
