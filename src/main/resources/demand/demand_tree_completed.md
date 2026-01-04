
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
