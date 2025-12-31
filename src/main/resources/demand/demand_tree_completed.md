
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
