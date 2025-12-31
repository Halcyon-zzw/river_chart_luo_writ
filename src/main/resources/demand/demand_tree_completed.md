
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
