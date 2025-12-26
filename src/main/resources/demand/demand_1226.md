# 需求描述
新增浏览表，只记录浏览的内容。

## 实现方案
采用方案B：每个用户对每个内容只记录一次（去重记录）

## 数据库表结构

**表名：** `browse_history`

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID |
| content_id | BIGINT | 内容ID |
| user_id | BIGINT | 用户ID（可为空，支持匿名浏览） |
| browse_count | INT | 浏览次数 |
| last_browse_time | DATETIME | 最后浏览时间 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| is_deleted | TINYINT | 删除标志：0-未删除，1-已删除 |

**索引：**
- 唯一键：`uk_content_user_deleted (content_id, user_id, is_deleted)`
- 普通索引：`idx_content_id`, `idx_user_id`, `idx_last_browse_time`

## 核心功能

### 1. 记录浏览
**接口：** `POST /browse-history/record`

**业务逻辑：**
- 如果记录不存在，插入新记录，`browse_count = 1`
- 如果记录已存在且未删除，增加 `browse_count`，更新 `last_browse_time`
- 如果记录已被逻辑删除，恢复记录并重置 `browse_count = 1`

### 2. 分页查询浏览历史
**接口：** `POST /browse-history/page`

**支持过滤：**
- 按用户ID查询（查询某个用户的浏览历史）
- 按内容ID查询（查询某个内容的浏览记录）
- 按最后浏览时间倒序排列

### 3. 获取内容浏览次数
**接口：** `GET /browse-history/content/{contentId}/count`

**返回：** 该内容的总浏览次数（所有用户的浏览次数之和）

### 4. 删除浏览记录
**接口：** `DELETE /browse-history/{id}`

**说明：** 逻辑删除

## 已生成文件

✅ SQL脚本：`browse_history.sql`
✅ Entity：`BrowseHistory.java`
✅ DTO：`BrowseHistoryDTO.java`
✅ Convert：`BrowseHistoryConvert.java`
✅ Mapper：`BrowseHistoryMapper.java` + `BrowseHistoryMapper.xml`
✅ Dao：`BrowseHistoryDao.java`
✅ Service：`BrowseHistoryService.java` + `BrowseHistoryServiceImpl.java`
✅ Controller：`BrowseHistoryController.java`
✅ Request：`RecordBrowseReq.java`, `BrowseHistoryPageReq.java`

## 使用示例

### 记录浏览
```json
POST /browse-history/record
{
  "contentId": 1,
  "userId": 100  // 可为空，表示匿名浏览
}
```

### 查询用户浏览历史
```json
POST /browse-history/page
{
  "pageNum": 1,
  "pageSize": 10,
  "userId": 100
}
```

### 查询内容浏览记录
```json
POST /browse-history/page
{
  "pageNum": 1,
  "pageSize": 10,
  "contentId": 1
}
```

### 获取内容浏览次数
```
GET /browse-history/content/1/count
```

---
**状态：** ✅ 已完成并编译成功