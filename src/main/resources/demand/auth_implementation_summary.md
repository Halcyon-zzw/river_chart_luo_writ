# 认证系统实现总结

## 已完成功能

### 1. 基础设施

#### JWT配置（支持Nacos动态刷新）
- **文件**: `JwtProperties.java`
- **配置项**:
  - `jwt.secret`: JWT签名密钥（至少32字符）
  - `jwt.access-token-expiration`: Access Token过期时间（默认2小时=7200秒）
  - `jwt.refresh-token-expiration`: Refresh Token过期时间（默认7天=604800秒）
  - `jwt.issuer`: JWT签发者

#### JWT工具类
- **文件**: `JwtUtil.java`
- **功能**:
  - 生成Access Token和Refresh Token
  - 验证Token有效性和类型
  - 从Token中提取用户信息
  - 检查Token是否即将过期

#### 验证码配置（支持Nacos动态刷新）
- **文件**: `VerificationCodeProperties.java`
- **配置项**:
  - `verification-code.expire-minutes`: 验证码有效期（默认5分钟）
  - `verification-code.send-interval-seconds`: 发送间隔（默认60秒）
  - `verification-code.max-send-times`: 最多发送次数（默认3次）
  - `verification-code.code-length`: 验证码长度（默认6位）

### 2. 数据库变更

#### User表优化
- **SQL文件**: `alter_user_table.sql`
- **新增字段**:
  - `email_verified`: 邮箱验证标志（0-未验证，1-已验证）
  - `phone_verified`: 手机号验证标志（0-未验证，1-已验证）
- **字段修改**:
  - `password_hash`: 改为可空（支持无密码登录场景）
  - `phone`: 添加唯一约束

#### 验证码表
- **SQL文件**: `verification_code.sql`
- **表结构**:
  - `id`: 主键
  - `target`: 目标（手机号或邮箱）
  - `code`: 验证码
  - `type`: 类型（PHONE_SMS/EMAIL）
  - `purpose`: 用途（REGISTER/LOGIN/RESET_PASSWORD/BIND）
  - `ip_address`: 请求IP
  - `expire_time`: 过期时间
  - `verified`: 是否已验证
  - `verify_time`: 验证时间
  - `create_time`: 创建时间

### 3. 认证接口

#### 3.1 用户注册
**接口**: `POST /auth/register`

**请求体**:
```json
{
  "username": "zhangsan",
  "password": "password123",
  "email": "zhangsan@example.com",
  "phone": "13800138000",
  "nickname": "张三"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1...",
    "refreshToken": "eyJhbGciOiJIUzI1...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userId": 1,
    "username": "zhangsan"
  }
}
```

**说明**:
- username必填，3-64个字符，只能包含字母、数字和下划线
- password必填，6-32个字符
- email、phone、nickname可选
- 注册成功后自动返回登录凭证

#### 3.2 密码登录
**接口**: `POST /auth/login/password`

**请求体**:
```json
{
  "account": "zhangsan",
  "password": "password123"
}
```

**说明**:
- account支持三种格式：用户名、手机号、邮箱
- 自动更新最后登录时间和IP

#### 3.3 验证码登录
**接口**: `POST /auth/login/code`

**请求体**:
```json
{
  "account": "13800138000",
  "code": "123456"
}
```

**说明**:
- account支持手机号或邮箱
- 首次登录自动注册账号
- 登录成功后自动标记为已验证

#### 3.4 发送验证码
**接口**: `POST /auth/send-code`

**请求体**:
```json
{
  "target": "13800138000",
  "type": "PHONE_SMS",
  "purpose": "LOGIN"
}
```

**参数说明**:
- `type`: PHONE_SMS（短信）或 EMAIL（邮件）
- `purpose`: REGISTER（注册）、LOGIN（登录）、RESET_PASSWORD（重置密码）、BIND（绑定）

**频率限制**:
- 同一目标1分钟内只能发送1次
- 5分钟内最多发送3次

#### 3.5 刷新令牌
**接口**: `POST /auth/refresh`

**请求体**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1..."
}
```

**说明**:
- 使用Refresh Token获取新的Access Token和Refresh Token
- Refresh Token有效期为7天

### 4. 验证码服务

#### 短信验证码
- **文件**: `VerificationCodeServiceImpl.java`
- **功能**: `sendSmsCode()`
- **状态**: 预留接口，当前仅记录数据库和日志
- **TODO**: 需要接入短信服务商（阿里云/腾讯云）

#### 邮件验证码
- **文件**: `VerificationCodeServiceImpl.java`
- **功能**: `sendEmailCode()`
- **状态**: 已实现，使用Spring Mail发送
- **配置**: 需要在Nacos中配置邮件服务器信息

### 5. Spring Security配置

**文件**: `SecurityConfig.java`

**放行路径**:
- `/auth/**` - 所有认证接口
- `/verification-code/**` - 验证码接口
- `/swagger-ui/**` - Swagger文档
- `/v3/api-docs/**` - OpenAPI文档

**特性**:
- 禁用CSRF（使用JWT不需要）
- 无状态会话管理
- 使用BCrypt加密密码

### 6. Nacos配置示例

**文件**: `nacos-config-example.yml`

**Data ID**: `river-chart-luo-writ-dev.yml`
**命名空间**: `dev`
**分组**: `DEFAULT_GROUP`

```yaml
# JWT配置
jwt:
  secret: your-256-bit-secret-key-change-this-in-production-environment-must-be-at-least-32-characters
  access-token-expiration: 7200
  refresh-token-expiration: 604800
  issuer: river-chart-luo-writ

# 验证码配置
verification-code:
  expire-minutes: 5
  send-interval-seconds: 60
  max-send-times: 3
  code-length: 6

# 邮件配置
spring:
  mail:
    host: smtp.qq.com
    port: 587
    username: your-email@qq.com
    password: your-auth-code
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
    default-encoding: UTF-8
```

## 使用流程

### 1. 注册并登录
```bash
# 1. 注册新用户
POST /auth/register
{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "phone": "13800138000"
}

# 响应中包含 accessToken 和 refreshToken
```

### 2. 验证码登录（首次自动注册）
```bash
# 1. 发送验证码
POST /auth/send-code
{
  "target": "13900139000",
  "type": "PHONE_SMS",
  "purpose": "LOGIN"
}

# 2. 使用验证码登录（如果用户不存在会自动注册）
POST /auth/login/code
{
  "account": "13900139000",
  "code": "123456"
}
```

### 3. 使用Access Token访问受保护资源
```bash
# 在请求头中携带token
Authorization: Bearer eyJhbGciOiJIUzI1...
```

### 4. Token过期后刷新
```bash
POST /auth/refresh
{
  "refreshToken": "eyJhbGciOiJIUzI1..."
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 4001 | 发送验证码过于频繁 |
| 4002 | 用户名已存在 |
| 4003 | 邮箱已被注册 |
| 4004 | 手机号已被注册 |
| 4005 | 账号或密码错误 |
| 4006 | 账号格式不正确 |
| 4007 | 验证码错误或已过期 |
| 4008 | 刷新令牌无效或已过期 |
| 4009 | 令牌类型错误 |
| 4010 | 令牌信息不完整 |
| 4011 | 用户不存在 |
| 4012 | 验证码类型不正确 |
| 4013 | 账号已被禁用 |
| 4014 | 账号已被锁定 |
| 4015 | 账号已过期 |
| 5001 | 邮件发送失败 |

## 待实现功能

### 1. 微信登录（已预留接口）
- 微信小程序登录
- 微信扫码登录（PC端）

### 2. 账号绑定
- 绑定/解绑手机号
- 绑定/解绑邮箱
- 绑定/解绑微信
- 设置/修改密码

### 3. 其他功能
- 密码重置
- 修改用户信息
- 注销账号

## 数据库初始化

```bash
# 1. 执行User表优化SQL
mysql -u dev_rw -p river_chart_luo_writ < src/main/resources/alter_user_table.sql

# 2. 创建验证码表
mysql -u dev_rw -p river_chart_luo_writ < src/main/resources/verification_code.sql
```

## 启动前准备

1. **配置Nacos**:
   - 启动Nacos服务器
   - 在Nacos中创建配置文件 `river-chart-luo-writ-dev.yml`
   - 按照 `nacos-config-example.yml` 配置内容

2. **执行数据库脚本**:
   - 执行User表优化SQL
   - 创建验证码表

3. **配置邮件服务**（如需使用邮件验证码）:
   - 在Nacos配置中填写SMTP服务器信息

4. **启动应用**:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **访问API文档**:
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - 查看并测试所有认证接口

## 安全建议

1. **生产环境配置**:
   - 修改JWT secret为强密钥（至少32个随机字符）
   - 启用HTTPS
   - 配置Redis缓存验证码（提升性能）

2. **验证码安全**:
   - 添加图形验证码防止恶意发送
   - 记录IP地址，限制单IP发送频率
   - 监控异常发送行为

3. **Token安全**:
   - 使用HTTPS传输token
   - Token存储在HttpOnly Cookie或内存中
   - 实现Token黑名单机制（登出时）

## 编译测试

```bash
# 编译项目
./mvnw clean compile

# 运行测试
./mvnw test

# 打包
./mvnw clean package -DskipTests
```

---

**实现状态**: ✅ 已完成并编译成功
**实现日期**: 2025-12-31
