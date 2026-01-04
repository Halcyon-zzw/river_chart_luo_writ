
## 2025-12-31: 浏览历史分页查询优化

### 需求概述
优化浏览历史分页查询功能，支持内容标题模糊查询和时间范围过滤。

### 实现功能

#### 1. 请求参数优化
- ✅ 删除 `contentId` 字段（不再支持按内容ID过滤）
- ✅ 新增 `contentTitle` 字段（支持内容标题模糊查询）
- ✅ 新增 `startTime` 字段（最后浏览时间范围-起）
- ✅ 新增 `endTime` 字段（最后浏览时间范围-止）

#### 2. 响应数据优化
- ✅ 在 `BrowseHistoryDTO` 中新增 `contentTitle` 字段
- ✅ 保留 `contentId` 字段（业务可能需要跳转）

#### 3. 查询逻辑优化
- ✅ 关联查询 `content` 表获取内容标题
- ✅ 支持按标题模糊查询（LIKE）
- ✅ 支持时间范围过滤（`last_browse_time`）
- ✅ 结果按 `last_browse_time` 降序排列

### 技术实现

**修改文件**:
- `BrowseHistoryPageReq.java` - 修改请求参数
- `BrowseHistoryDTO.java` - 新增 contentTitle 字段
- `BrowseHistoryMapper.java` - 新增 selectPageWithContentTitle 方法
- `BrowseHistoryMapper.xml` - 实现关联查询SQL
- `BrowseHistoryServiceImpl.java` - 使用新的查询方法

**SQL查询**:
```sql
SELECT
    bh.id,
    bh.content_id,
    c.title AS content_title,
    bh.user_id,
    bh.browse_count,
    bh.last_browse_time,
    bh.create_time,
    bh.update_time,
    bh.is_deleted
FROM browse_history bh
LEFT JOIN content c ON bh.content_id = c.id AND c.is_deleted = 0
WHERE bh.is_deleted = 0
  AND (条件过滤...)
ORDER BY bh.last_browse_time DESC
```

### 查询条件

| 参数 | 类型 | 说明 | 示例 |
|------|------|------|------|
| userId | Long | 用户ID（可选） | 1 |
| contentTitle | String | 内容标题（模糊查询，可选） | "Spring Boot" |
| startTime | LocalDateTime | 开始时间（可选） | 2025-01-01T00:00:00 |
| endTime | LocalDateTime | 结束时间（可选） | 2025-12-31T23:59:59 |
| pageNum | Integer | 页码 | 1 |
| pageSize | Integer | 每页数量 | 10 |

### 使用示例

**查询某个用户最近一周的浏览历史**:
```json
POST /browse-history/page
{
  "userId": 1,
  "startTime": "2025-12-24T00:00:00",
  "endTime": "2025-12-31T23:59:59",
  "pageNum": 1,
  "pageSize": 10
}
```

**按标题搜索浏览历史**:
```json
POST /browse-history/page
{
  "contentTitle": "Spring",
  "pageNum": 1,
  "pageSize": 10
}
```

### 状态
✅ 已完成并编译成功

---

## 2025-12-31: 浏览历史新增内容类型过滤

### 需求概述
在浏览历史分页查询中新增内容类型字段，支持按内容类型（图片/笔记）过滤浏览记录。

### 实现功能

#### 1. 请求参数新增
- ✅ 在 `BrowseHistoryPageReq` 中新增 `contentType` 字段
- ✅ 支持按内容类型过滤（`image`-图片, `note`-笔记）

#### 2. 响应数据新增
- ✅ 在 `BrowseHistoryDTO` 中新增 `contentType` 字段
- ✅ 返回每条浏览记录的内容类型

#### 3. 查询逻辑优化
- ✅ 关联查询 `content` 表的 `content_type` 字段
- ✅ 支持按内容类型精确过滤

### 技术实现

**修改文件**:
- `BrowseHistoryPageReq.java` - 新增 contentType 过滤字段
- `BrowseHistoryDTO.java` - 新增 contentType 返回字段
- `BrowseHistoryMapper.xml` - 更新SQL查询和ResultMap

**SQL查询增强**:
```sql
SELECT
    bh.id,
    bh.content_id,
    c.title AS content_title,
    c.content_type AS content_type,  -- 新增
    bh.user_id,
    bh.browse_count,
    bh.last_browse_time,
    ...
FROM browse_history bh
LEFT JOIN content c ON bh.content_id = c.id AND c.is_deleted = 0
WHERE bh.is_deleted = 0
  AND c.content_type = ?  -- 新增过滤条件
ORDER BY bh.last_browse_time DESC
```

### 使用示例

**查询图片类型的浏览历史**:
```json
POST /browse-history/page
{
  "contentType": "image",
  "pageNum": 1,
  "pageSize": 10
}
```

**查询笔记类型的浏览历史**:
```json
POST /browse-history/page
{
  "contentType": "note",
  "pageNum": 1,
  "pageSize": 10
}
```

**组合查询（某用户的图片浏览历史）**:
```json
POST /browse-history/page
{
  "userId": 1,
  "contentType": "image",
  "startTime": "2025-12-01T00:00:00",
  "endTime": "2025-12-31T23:59:59",
  "pageNum": 1,
  "pageSize": 10
}
```

### 支持的内容类型

| 类型值 | 说明 |
|--------|------|
| image | 图片内容 |
| note | 笔记内容 |

### 状态
✅ 已完成并编译成功

---

## 2025-12-31: 用户上下文系统（JWT认证拦截器）

### 需求概述
实现基于JWT的用户上下文系统，通过拦截器从请求头中解析JWT令牌，提取用户ID并存储到ThreadLocal中，支持在项目中任何地方调用 `UserUtil.getUserId()` 获取当前用户ID。

### 实现功能

#### 1. ThreadLocal用户上下文
- ✅ 创建 `UserContext` 类，使用ThreadLocal存储用户ID
- ✅ 提供 `setUserId()`、`getUserId()`、`clear()` 方法
- ✅ 默认用户ID为 `-1`（未登录或token验证失败）

#### 2. 用户工具类
- ✅ 创建 `UserUtil` 工具类
- ✅ 提供静态方法 `getUserId()`，可在项目中任何地方调用
- ✅ 无需传递 `HttpServletRequest` 参数

#### 3. JWT工具类
- ✅ 基于 Hutool JWT 实现
- ✅ 支持生成 Access Token 和 Refresh Token（双令牌策略）
- ✅ 支持从令牌中提取用户ID
- ✅ 支持从 `Authorization: Bearer <token>` 请求头中提取令牌
- ✅ 完整的令牌验证（签名验证、过期时间验证）

#### 4. JWT认证拦截器
- ✅ 实现 `HandlerInterceptor` 接口
- ✅ 在 `preHandle` 中解析JWT令牌并设置用户ID到ThreadLocal
- ✅ 在 `afterCompletion` 中清理ThreadLocal，防止内存泄漏
- ✅ Token验证失败时设置userId为 `-1`，允许匿名访问

#### 5. 拦截器注册
- ✅ 在 `WebMvcConfig` 中注册JWT拦截器
- ✅ 拦截所有路径（`/**`）
- ✅ 排除公开接口：
  - `/auth/**` - 认证相关接口
  - `/verification-code/**` - 验证码接口
  - `/swagger-ui/**` - Swagger UI
  - `/v3/api-docs/**` - OpenAPI文档

### 技术实现

**新增文件**:
- `UserContext.java` - ThreadLocal用户上下文
- `UserUtil.java` - 用户工具类
- `JwtUtil.java` - JWT工具类（基于Hutool）
- `JwtAuthenticationInterceptor.java` - JWT认证拦截器

**修改文件**:
- `WebMvcConfig.java` - 注册JWT拦截器
- `pom.xml` - 添加 `hutool-jwt` 依赖

**拦截器执行流程**:
```
Request (Authorization: Bearer <token>)
  ↓
JwtAuthenticationInterceptor.preHandle()
  ├─ 提取JWT令牌
  ├─ 解析令牌，提取userId
  ├─ 验证签名和过期时间
  ├─ 存储到 UserContext (ThreadLocal)
  └─ 失败时设置 userId = -1
  ↓
Controller/Service 调用 UserUtil.getUserId()
  └─ 从 ThreadLocal 获取 userId
  ↓
JwtAuthenticationInterceptor.afterCompletion()
  └─ UserContext.clear() (清理ThreadLocal)
```

### JWT配置参数

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| jwt.secret | JWT密钥 | river-chart-luo-writ-secret-key-2025 |
| jwt.access-token-expire | Access Token过期时间（秒） | 7200（2小时） |
| jwt.refresh-token-expire | Refresh Token过期时间（秒） | 604800（7天） |

### 使用示例

**在Controller中获取当前用户ID**:
```java
@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/profile")
    public Result<UserProfile> getProfile() {
        // 无需传递HttpServletRequest，直接获取用户ID
        Long userId = UserUtil.getUserId();

        if (userId == -1) {
            // 未登录用户
            return Result.error(401, "请先登录");
        }

        // 已登录用户逻辑
        return Result.success(userService.getProfile(userId));
    }
}
```

**在Service中获取当前用户ID**:
```java
@Service
public class MyServiceImpl implements MyService {

    public void doSomething() {
        Long userId = UserUtil.getUserId();
        log.info("当前用户ID: {}", userId);
    }
}
```

**生成JWT令牌**:
```java
@Autowired
private JwtUtil jwtUtil;

// 生成Access Token
String accessToken = jwtUtil.generateAccessToken(userId);

// 生成Refresh Token
String refreshToken = jwtUtil.generateRefreshToken(userId);
```

### 安全特性

1. **线程安全**: 使用ThreadLocal确保多线程环境下用户上下文隔离
2. **内存安全**: 请求完成后自动清理ThreadLocal，防止内存泄漏
3. **匿名访问**: Token验证失败时不阻断请求，允许部分接口支持匿名访问
4. **签名验证**: 使用密钥验证JWT签名，防止令牌被篡改
5. **过期检查**: 自动检查令牌是否过期

### 状态
✅ 已完成并编译成功

---

## 2025-12-31: 浏览历史分页接口响应优化

### 需求概述
优化浏览历史分页接口响应结构，将扁平化的内容字段（contentTitle、contentType）整合为嵌套的 contentDTO 对象，提供完整的内容信息。

### 实现功能

#### 1. BrowseHistoryDTO 结构调整
- ✅ 删除 `contentTitle` 字段（扁平化字段）
- ✅ 删除 `contentType` 字段（扁平化字段）
- ✅ 保留 `contentId` 字段（方便直接引用）
- ✅ 新增 `contentDTO` 字段（嵌套完整的 ContentDTO 对象）

#### 2. 查询逻辑优化
- ✅ LEFT JOIN content 表获取所有内容字段
- ✅ 使用 MyBatis `<association>` 标签映射嵌套对象
- ✅ 支持已删除内容的返回（is_deleted = 1 的内容也会返回）
- ✅ contentDTO 包含完整内容信息（包括已删除标记）

#### 3. ResultMap 增强
- ✅ 创建支持嵌套 ContentDTO 的 ResultMap
- ✅ 为 content 表的所有字段设置别名（前缀 `c_`）
- ✅ 正确映射所有字段到 ContentDTO

### 技术实现

**修改文件**:
- `BrowseHistoryDTO.java` - 调整字段结构
- `BrowseHistoryMapper.xml` - 增强 ResultMap 和 SQL 查询

**ResultMap 结构**:
```xml
<resultMap id="DTOResultMap" type="BrowseHistoryDTO">
    <id column="id" property="id" />
    <result column="content_id" property="contentId" />
    <!-- ... 其他浏览历史字段 ... -->

    <!-- 嵌套 ContentDTO -->
    <association property="contentDTO" javaType="ContentDTO">
        <id column="c_id" property="id" />
        <result column="c_title" property="title" />
        <result column="c_content_type" property="contentType" />
        <result column="c_is_deleted" property="isDeleted" />
        <!-- ... 其他内容字段 ... -->
    </association>
</resultMap>
```

**SQL 查询优化**:
```sql
SELECT
    bh.id,
    bh.content_id,
    bh.user_id,
    bh.browse_count,
    bh.last_browse_time,
    -- ... 其他浏览历史字段 ...
    c.id AS c_id,
    c.title AS c_title,
    c.content_type AS c_content_type,
    c.image_url AS c_image_url,
    c.is_deleted AS c_is_deleted,
    -- ... 其他内容字段 ...
FROM browse_history bh
LEFT JOIN content c ON bh.content_id = c.id  -- 不再过滤已删除内容
WHERE bh.is_deleted = 0
ORDER BY bh.last_browse_time DESC
```

### 响应数据结构

**优化前**（扁平化结构）:
```json
{
  "id": 1,
  "contentId": 100,
  "contentTitle": "Spring Boot教程",
  "contentType": "note",
  "userId": 1,
  "browseCount": 5,
  "lastBrowseTime": "2025-12-31T10:00:00"
}
```

**优化后**（嵌套结构）:
```json
{
  "id": 1,
  "contentId": 100,
  "contentDTO": {
    "id": 100,
    "subCategoryId": 10,
    "title": "Spring Boot教程",
    "contentType": "note",
    "noteContent": "教程内容...",
    "noteFormat": "markdown",
    "description": "详细描述",
    "sortOrder": 1,
    "isDeleted": 0,
    "createTime": "2025-12-20T10:00:00",
    "updateTime": "2025-12-30T15:00:00"
  },
  "userId": 1,
  "browseCount": 5,
  "lastBrowseTime": "2025-12-31T10:00:00"
}
```

**已删除内容示例**:
```json
{
  "id": 2,
  "contentId": 101,
  "contentDTO": {
    "id": 101,
    "title": "已删除的内容",
    "contentType": "image",
    "isDeleted": 1,  // 标记为已删除
    "createTime": "2025-12-15T10:00:00",
    "updateTime": "2025-12-31T10:00:00"
  },
  "userId": 1,
  "browseCount": 3,
  "lastBrowseTime": "2025-12-31T09:00:00"
}
```

### ContentDTO 包含的字段

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 内容ID |
| subCategoryId | Long | 所属小分类ID |
| title | String | 内容标题 |
| contentType | String | 内容类型（image/note） |
| imageUrl | String | 图片URL（图片类型） |
| imageThumbnailUrl | String | 缩略图URL |
| imageSize | Long | 图片大小（字节） |
| imageWidth | Integer | 图片宽度 |
| imageHeight | Integer | 图片高度 |
| noteContent | String | 笔记内容（笔记类型） |
| noteFormat | String | 笔记格式 |
| description | String | 内容描述 |
| sortOrder | Integer | 排序权重 |
| isDeleted | Byte | 删除标志（0-未删除，1-已删除） |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |

### 优势

1. **结构清晰**: 内容信息集中在 contentDTO 中，更符合面向对象设计
2. **扩展性强**: 后续添加内容字段无需修改 BrowseHistoryDTO
3. **信息完整**: 提供完整的内容信息，减少额外的查询请求
4. **已删除内容处理**: 即使内容被删除，仍保留基本信息供查看
5. **向下兼容**: 保留 contentId 字段，方便直接引用

### 注意事项

**关于其他 Service 方法**:
- `page()` 方法：✅ 返回完整的 contentDTO
- `recordBrowse()` 方法：contentDTO 为 null（仅记录浏览行为，不返回内容详情）
- `getById()` 方法：contentDTO 为 null（如需完整信息，请使用 page 方法）

如需在其他方法中返回 contentDTO，需要在 Mapper 中添加对应的关联查询方法。

### 状态
✅ 已完成并编译成功

---

## 2025-12-31: 修复分页查询total返回0的问题

### 需求概述
修复项目中所有分页查询的 total 字段返回 0 的问题，影响范围包括自定义Mapper方法和MyBatis Plus BaseMapper的分页查询。

### 问题分析

#### 问题现象
所有分页查询的返回结果中，`total`（总记录数）字段始终为 0，导致前端无法正确显示分页信息。

**受影响的查询**:
1. `BrowseHistoryMapper.selectPageWithContentTitle()` - 浏览历史分页查询
2. `MainCategoryDao.page()` - 主分类分页查询
3. 其他所有使用 MyBatis Plus 分页的查询

#### 根本原因
`MybatisPlusConfig` 中**缺少分页插件配置**。MyBatis Plus 的分页功能依赖 `PaginationInnerInterceptor` 拦截器：
- 拦截器会自动执行 COUNT 查询获取总记录数
- 拦截器会自动执行分页查询获取当前页数据
- 没有拦截器，Page 对象的 total 字段默认为 0

**原配置**（仅有乐观锁插件）:
```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    return interceptor;
}
```

### 解决方案

#### 添加分页插件配置

在 `MybatisPlusConfig` 中添加 `PaginationInnerInterceptor` 分页拦截器。

**修改后的完整配置**:
```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    // 1. 添加分页插件（必须在最前面）
    PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
    // 设置请求的页面大于最大页后的操作，true返回首页，false继续请求
    paginationInterceptor.setOverflow(false);
    // 单页分页条数限制，默认无限制
    paginationInterceptor.setMaxLimit(1000L);
    interceptor.addInnerInterceptor(paginationInterceptor);

    // 2. 添加乐观锁插件
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

    return interceptor;
}
```

### 技术实现

**修改文件**:
- `MybatisPlusConfig.java` - 添加分页插件配置

**关键配置说明**:
1. **DbType.MYSQL**: 指定数据库类型为 MySQL，确保生成正确的分页SQL
2. **setOverflow(false)**: 当请求页码超过最大页时，继续请求（不返回首页）
3. **setMaxLimit(1000L)**: 限制单页最大查询 1000 条数据，防止大量数据查询
4. **插件顺序**: 分页插件必须添加在最前面，确保优先执行

### 分页插件工作原理

1. **拦截分页查询**: 当检测到方法参数中有 `IPage` 对象时，自动拦截
2. **执行 COUNT 查询**:
   ```sql
   SELECT COUNT(*) FROM (原始SQL) AS total
   ```
3. **执行分页查询**:
   ```sql
   原始SQL LIMIT #{offset}, #{pageSize}
   ```
4. **封装结果**: 将 count 结果设置到 Page 对象的 total 字段

### 验证方法

**测试分页查询**:
```java
// 1. 浏览历史分页查询
Page<BrowseHistoryDTO> page = new Page<>(1, 10);
IPage<BrowseHistoryDTO> result = browseHistoryMapper.selectPageWithContentTitle(page, req);
System.out.println("Total: " + result.getTotal()); // 应该返回正确的总数

// 2. 主分类分页查询
Page<MainCategory> page = new Page<>(1, 10);
Page<MainCategory> result = mainCategoryDao.page(page, queryWrapper);
System.out.println("Total: " + result.getTotal()); // 应该返回正确的总数
```

### 影响范围

**修复后的功能**:
- ✅ 浏览历史分页查询
- ✅ 主分类分页查询
- ✅ 子分类分页查询
- ✅ 内容分页查询
- ✅ 收藏分页查询
- ✅ 所有其他使用 MyBatis Plus 分页的查询

### 注意事项

1. **插件顺序很重要**: 分页插件必须在其他插件之前添加
2. **数据库类型**: 确保 `DbType` 与实际数据库一致（MySQL、Oracle、PostgreSQL等）
3. **性能考虑**:
   - COUNT 查询会增加一次数据库查询
   - 对于大数据量表，COUNT 查询可能较慢
   - 可以考虑使用 `setOptimizeCountSql(true)` 优化 COUNT SQL
4. **自定义 Mapper**: 分页插件对自定义 XML 的分页查询同样有效

### 扩展配置（可选）

**优化 COUNT 查询性能**:
```java
PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
// 优化COUNT SQL，自动去除 ORDER BY 等不必要的语句
paginationInterceptor.setOptimizeJoin(true);
```

**禁用某些查询的 COUNT**:
```java
// 在查询时设置不执行 count
page.setOptimizeCountSql(false); // 优化count查询
page.setSearchCount(false);      // 完全不执行count
```

### 状态
✅ 已完成并编译成功

---

## 2026-01-04: 修复JWT拦截器不执行的问题

### 需求概述
修复 JwtAuthenticationInterceptor 拦截器无法执行的问题，导致用户无法看到拦截器的执行日志。

### 问题分析

#### 问题现象
虽然 JWT 拦截器已正确配置并注册到 WebMvcConfig 中，但运行时看不到任何拦截器的日志输出，导致用户误以为拦截器没有执行。

#### 根本原因
**日志配置错误**：`application.yml` 中的日志配置为 `com.example: DEBUG`，但项目的包名是 `com.dzy.river.chart.luo.writ`，导致拦截器的日志无法输出。

**原日志配置**:
```yaml
logging:
  level:
    com.example: DEBUG  # 错误：包名不匹配
```

**拦截器日志级别**: 拦截器的所有日志都使用 `log.debug()`，在没有正确配置包路径的情况下，这些日志不会输出。

### 解决方案

#### 1. 修复日志配置

**修改 application.yml**:
```yaml
logging:
  level:
    com.dzy.river.chart.luo.writ: DEBUG
    com.dzy.river.chart.luo.writ.interceptor: INFO  # 拦截器日志设置为INFO级别
```

#### 2. 提升拦截器日志级别

将拦截器中的关键日志从 `DEBUG` 级别提升为 `INFO` 级别，确保在默认配置下也能看到拦截器的执行情况。

**修改前**（使用 DEBUG）:
```java
log.debug("Processing request: {} {}", request.getMethod(), requestURI);
log.debug("User authenticated successfully, userId: {}", userId);
log.debug("ThreadLocal cleaned up for request: {}", request.getRequestURI());
```

**修改后**（使用 INFO）:
```java
log.info("JWT拦截器执行 - 请求路径: {} {}", request.getMethod(), requestURI);
log.info("JWT认证成功 - userId: {}", userId);
log.info("JWT拦截器清理 - ThreadLocal已清理，请求路径: {}", request.getRequestURI());
```

### 技术实现

**修改文件**:
- `application.yml` - 修复日志包路径配置
- `JwtAuthenticationInterceptor.java` - 提升关键日志级别为 INFO

**日志输出示例**:
```
INFO  [http-nio-8080-exec-1] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT拦截器执行 - 请求路径: GET /api/browse-history/page
INFO  [http-nio-8080-exec-1] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : 未找到Authorization请求头，使用默认userId: -1 (匿名访问)
INFO  [http-nio-8080-exec-1] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT拦截器完成 - userId已设置为: -1
INFO  [http-nio-8080-exec-1] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT拦截器清理 - ThreadLocal已清理，请求路径: /api/browse-history/page
```

**带Token的请求日志**:
```
INFO  [http-nio-8080-exec-2] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT拦截器执行 - 请求路径: GET /api/user/profile
INFO  [http-nio-8080-exec-2] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT认证成功 - userId: 123
INFO  [http-nio-8080-exec-2] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT拦截器完成 - userId已设置为: 123
INFO  [http-nio-8080-exec-2] c.d.r.c.l.w.i.JwtAuthenticationInterceptor : JWT拦截器清理 - ThreadLocal已清理，请求路径: /api/user/profile
```

### 验证方法

1. **启动应用**:
   ```bash
   ./mvnw spring-boot:run
   ```

2. **访问任意接口**（不带Token）:
   ```bash
   curl http://localhost:8080/api/browse-history/page
   ```
   应该看到日志：`未找到Authorization请求头，使用默认userId: -1 (匿名访问)`

3. **访问接口**（带Token）:
   ```bash
   curl -H "Authorization: Bearer <token>" http://localhost:8080/api/user/profile
   ```
   应该看到日志：`JWT认证成功 - userId: <用户ID>`

### 拦截器执行流程

```
请求到达
  ↓
JwtAuthenticationInterceptor.preHandle()
  ├─ 日志：JWT拦截器执行 - 请求路径: ...
  ├─ 解析 Authorization 头
  ├─ 提取 JWT Token
  ├─ 验证 Token 并提取 userId
  ├─ 存储到 ThreadLocal
  ├─ 日志：JWT拦截器完成 - userId已设置为: ...
  └─ 返回 true（继续执行）
  ↓
Controller 处理请求
  ├─ 可以通过 UserUtil.getUserId() 获取 userId
  └─ 业务逻辑执行
  ↓
JwtAuthenticationInterceptor.afterCompletion()
  ├─ 清理 ThreadLocal
  ├─ 日志：JWT拦截器清理 - ThreadLocal已清理，请求路径: ...
  └─ 请求完成
```

### 注意事项

1. **日志级别建议**:
   - 开发环境：使用 INFO 级别，便于调试
   - 生产环境：可调整为 WARN 级别，减少日志量

2. **包路径配置**:
   - 确保日志配置的包路径与实际项目包名一致
   - 可以为不同的包设置不同的日志级别

3. **拦截器顺序**:
   - JWT 拦截器应该在业务拦截器之前执行
   - 确保 UserContext 在业务逻辑中可用

4. **排除路径**:
   - 公开接口（`/auth/**`、`/swagger-ui/**` 等）已正确排除
   - 这些接口不会执行拦截器

### 状态
✅ 已完成并编译成功

---

## 2026-01-04: 浏览历史清空接口

### 需求概述
新增清空浏览历史接口，允许用户一键清空自己的所有浏览历史记录。用户ID从 UserUtil 中自动获取，无需前端传递。

### 实现功能

#### 1. Service 层方法
- ✅ 在 `BrowseHistoryService` 接口中新增 `clearByUserId(Long userId)` 方法
- ✅ 在 `BrowseHistoryServiceImpl` 中实现该方法
- ✅ 使用逻辑删除（设置 `is_deleted = 1`）
- ✅ 返回清空的记录数

#### 2. Controller 层接口
- ✅ 在 `BrowseHistoryController` 中新增 `DELETE /browse-history/clear` 接口
- ✅ 从 `UserUtil.getUserId()` 获取当前用户ID
- ✅ 调用 Service 层方法清空浏览历史
- ✅ 返回清空的记录数

#### 3. 用户身份识别
- ✅ 使用 `UserUtil.getUserId()` 从 ThreadLocal 获取用户ID
- ✅ 支持已登录用户（userId > 0）清空历史
- ✅ 支持匿名用户（userId = -1）清空历史

### 技术实现

**新增方法**:

**BrowseHistoryService.java**:
```java
/**
 * 清空指定用户的所有浏览历史
 *
 * @param userId 用户ID
 * @return 清空的记录数
 */
Integer clearByUserId(Long userId);
```

**BrowseHistoryServiceImpl.java**:
```java
@Override
@Transactional(rollbackFor = Exception.class)
public Integer clearByUserId(Long userId) {
    // 构建查询条件：根据 userId 查询未删除的浏览历史
    LambdaQueryWrapper<BrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(BrowseHistory::getUserId, userId);
    queryWrapper.eq(BrowseHistory::getIsDeleted, 0);

    // 查询符合条件的记录数
    long count = browseHistoryDao.count(queryWrapper);

    if (count > 0) {
        // 逻辑删除：设置 is_deleted = 1
        BrowseHistory updateEntity = new BrowseHistory();
        updateEntity.setIsDeleted((byte) 1);
        browseHistoryDao.update(updateEntity, queryWrapper);
    }

    return (int) count;
}
```

**BrowseHistoryController.java**:
```java
/**
 * 清空当前用户的浏览历史
 */
@DeleteMapping("/clear")
@Operation(summary = "清空浏览历史", description = "清空当前用户的所有浏览历史记录")
public Result<Integer> clearBrowseHistory() {
    // 从 ThreadLocal 中获取当前用户ID
    Long userId = UserUtil.getUserId();

    // 清空该用户的所有浏览历史
    Integer count = browseHistoryService.clearByUserId(userId);

    return Result.success("已清空 " + count + " 条浏览历史", count);
}
```

### 接口说明

**请求**:
```http
DELETE /browse-history/clear
Authorization: Bearer <token>  # 可选，不传则清空匿名浏览历史
```

**响应**（成功）:
```json
{
  "code": 200,
  "message": "已清空 15 条浏览历史",
  "data": 15
}
```

**响应**（无记录）:
```json
{
  "code": 200,
  "message": "已清空 0 条浏览历史",
  "data": 0
}
```

### 业务逻辑

1. **获取用户ID**: 从 `UserUtil.getUserId()` 获取当前用户ID
2. **查询记录数**: 统计该用户未删除的浏览历史记录数
3. **逻辑删除**: 将所有符合条件的记录设置 `is_deleted = 1`
4. **返回结果**: 返回清空的记录数

### 用户场景

#### 已登录用户清空
```bash
# 请求（带 Token）
curl -X DELETE http://localhost:8080/browse-history/clear \
  -H "Authorization: Bearer eyJhbGc..."

# 响应
{
  "code": 200,
  "message": "已清空 15 条浏览历史",
  "data": 15
}
```

#### 匿名用户清空
```bash
# 请求（不带 Token）
curl -X DELETE http://localhost:8080/browse-history/clear

# 响应
{
  "code": 200,
  "message": "已清空 5 条浏览历史",
  "data": 5
}
```

### 技术特性

1. **逻辑删除**: 使用逻辑删除而非物理删除，保留数据可恢复性
2. **事务管理**: 使用 `@Transactional` 确保数据一致性
3. **性能优化**: 先统计记录数，有记录时才执行更新操作
4. **用户隔离**: 只清空当前用户的浏览历史，不影响其他用户
5. **无需传参**: 用户ID从 ThreadLocal 自动获取，前端无需传递

### 数据库操作

**SQL 示例**:
```sql
-- 统计记录数
SELECT COUNT(*) FROM browse_history 
WHERE user_id = ? AND is_deleted = 0;

-- 逻辑删除
UPDATE browse_history 
SET is_deleted = 1 
WHERE user_id = ? AND is_deleted = 0;
```

### 注意事项

1. **不校验登录状态**: 允许匿名用户（userId = -1）清空浏览历史
2. **幂等性**: 多次调用清空接口，第二次开始返回 0（已无记录可清空）
3. **数据恢复**: 使用逻辑删除，可在数据库层面恢复误删数据
4. **性能考虑**: 清空操作使用批量更新，性能良好

### 修改文件

- `BrowseHistoryService.java` - 新增 clearByUserId 接口方法
- `BrowseHistoryServiceImpl.java` - 实现 clearByUserId 方法
- `BrowseHistoryController.java` - 新增 DELETE /clear 接口

### 状态
✅ 已完成并编译成功

---
