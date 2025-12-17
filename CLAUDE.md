# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

River Chart Luo Writ 是一个基于 Spring Boot 3 的内容管理系统，用于组织和管理层级分类结构的内容（图片和笔记）并支持标签功能。系统提供用户认证、收藏功能和操作日志记录。

## 技术栈

- **框架**: Spring Boot 3.3.5 (Java 17)
- **ORM**: MyBatis-Plus 3.5.7（支持乐观锁）
- **数据库**: MySQL 8.x（连接池：HikariCP）
- **API 文档**: SpringDoc OpenAPI 2.6.0
- **对象映射**: MapStruct 1.5.x（用于 Entity ↔ DTO 转换）
- **构建工具**: Maven

## 常用命令

### 开发相关
```bash
# 运行应用
./mvnw spring-boot:run

# 使用指定 profile 运行
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 运行所有测试
./mvnw test

# 运行单个测试类
./mvnw test -Dtest=ContentsServiceTest

# 运行单个测试方法
./mvnw test -Dtest=ContentsServiceTest#testGetById

# 构建（跳过测试）
./mvnw clean package -DskipTests

# 构建（包含测试）
./mvnw clean package
```

### 数据库相关
```bash
# 初始化数据库表结构
mysql -u dev_rw -p river_chart_luo_writ < src/main/resources/create.sql

# 应用索引优化
mysql -u dev_rw -p river_chart_luo_writ < src/main/resources/optimize_indexes.sql
```

## 代码架构

### 包结构

```
com.dzy.river.chart.luo.writ/
├── common/          # 通用工具类（Result、ResultCode）
├── config/          # Spring 配置类（MybatisPlusConfig）
├── controller/      # REST 控制器
├── convert/         # MapStruct 转换器（Entity ↔ DTO）
├── dao/             # 数据访问层（当前未使用，逻辑在 Service 层）
├── dto/             # 数据传输对象
├── entity/          # 数据库实体类
├── exception/       # 自定义异常（BaseException、BusinessException、DataNotFoundException）
├── handler/         # 全局异常处理器
├── mapper/          # MyBatis Mapper 接口
└── service/         # 业务逻辑层
    └── impl/        # Service 实现类
```

### 分层架构

**Controller → Service → Mapper → Database**

1. **Controller 层**：REST 端点，带 Swagger 注解
   - 所有响应使用 `Result<T>` 包装器
   - 使用 `@Validated` 进行输入校验
   - 404 场景抛出 `DataNotFoundException`

2. **Service 层**：业务逻辑和事务管理
   - 采用接口 + 实现类模式
   - 使用 MapStruct 转换器在 Entity 和 DTO 之间转换
   - 示例：`ContentsService` / `ContentsServiceImpl`

3. **Mapper 层**：MyBatis 数据访问
   - XML 映射文件位于 `src/main/resources/mapper/`
   - MyBatis-Plus BaseMapper 提供基础 CRUD 操作
   - 配置了乐观锁拦截器

4. **Entity/DTO 分离**：
   - **Entity**：数据库表示（如 `Contents.java`）
   - **DTO**：API 表示（如 `ContentsDTO.java`）
   - **Convert**：MapStruct 接口，用于双向转换

### 领域模型

应用管理一个层级化的内容结构：

```
User（用户认证、角色、微信集成）
  └─ UserFavorites（收藏内容）

MainCategories（主分类）
  └─ SubCategories（子分类）
      └─ Contents（图片或笔记内容）

Tags（标签，通过关联表应用于分类和内容）
  ├─ MainCategoryTags（主分类标签关联）
  ├─ SubCategoryTags（子分类标签关联）
  └─ ContentTags（内容标签关联）

OperationLogs（操作审计日志）
```

**关键概念**：
- **逻辑删除**：所有表使用 `is_deleted` 标志（0=未删除，1=已删除）
- **内容类型**：Contents 可以是 `image`（带 URL/大小/尺寸）或 `note`（带文本/格式）
- **标签系统**：Tags 与 Categories/Contents 之间的多对多关系
- **微信支持**：User 表包含 `openid`、`unionid` 和 `wechat_info` JSON 字段

### 异常处理

自定义异常层级：
- `BaseException` → 所有自定义异常的基类
- `BusinessException` → 业务逻辑错误（返回 200 状态码 + 错误码）
- `DataNotFoundException` → 资源不存在（返回 404）

所有异常由 `GlobalExceptionHandler` 捕获并转换为 `Result<T>` 响应。

### 响应格式

所有 API 响应使用 `Result<T>` 包装器：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

错误响应：
```json
{
  "code": 7001,
  "message": "数据不存在",
  "data": null
}
```

## 数据库说明

### 连接配置
- 地址：`10.95.16.85:3306`
- 数据库名：`river_chart_luo_writ`
- 用户名：`dev_rw`
- 连接池：HikariCP（最大 10，最小 5）

### 索引优化
`optimize_indexes.sql` 脚本会删除低效的 `is_deleted` 单列索引，并为常见查询模式创建组合索引。**必须在初始化表结构后执行此脚本**。

### MyBatis-Plus 配置
- **乐观锁**：通过 `OptimisticLockerInnerInterceptor` 启用
- **Mapper 扫描**：`com.dzy.river.chart.luo.writ.mapper`
- **XML 位置**：`classpath:mapper/*.xml`

## 代码生成

项目使用 MyBatis-Plus Generator 配合 FreeMarker 模板生成代码。生成新实体时：

1. 生成内容：Entity、Mapper（接口 + XML）、Service（接口 + 实现）、Controller、DTO、Convert
2. 遵循现有命名规范：
   - Entity：大驼峰（如 `Contents`）
   - DTO：实体名 + DTO（如 `ContentsDTO`）
   - Service：实体名 + Service（如 `ContentsService`）
   - Convert：实体名 + Convert（如 `ContentsConvert`）

## 重要模式

### MapStruct 转换器
所有转换器使用统一模式：
```java
@Mapper(componentModel = "spring")
public interface ContentsConvert {
    ContentsDTO toDTO(Contents entity);
    Contents toEntity(ContentsDTO dto);
    List<ContentsDTO> toDTOList(List<Contents> entities);
}
```

### Service 实现模式
```java
@Service
public class ContentsServiceImpl implements ContentsService {
    @Autowired
    private ContentsMapper mapper;

    @Autowired
    private ContentsConvert convert;

    @Override
    public ContentsDTO getById(Long id) {
        Contents entity = mapper.selectById(id);
        return convert.toDTO(entity);
    }
}
```

### 逻辑删除查询
查询时必须过滤 `is_deleted = 0`，除非明确需要检索已删除记录。

## API 文档

- **Swagger UI**：`http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**：`http://localhost:8080/v3/api-docs`

所有 Controller 使用 `@Tag`（类级别）和 `@Operation`（方法级别）注解进行文档化。

## 配置说明

主配置文件：`src/main/resources/application.yml`
- 服务端口：8080
- 数据库连接设置
- 日志级别（`com.example` 设置为 DEBUG）
- 自定义应用配置在 `myapp.*` 下

## 测试

测试类遵循命名规范 `*Test.java` 或 `*Tests.java`，位于 `src/test/java/`。

基础测试类：`RiverChartLuoWritApplicationTests` 用于验证应用上下文加载。
