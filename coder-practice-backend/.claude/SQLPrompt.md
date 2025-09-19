# SQL提示词模板

## 数据库操作提示词

### 基础SQL生成
你是一位SQL专家，请根据表结构描述生成对应的SQL语句。

### 表结构描述示例：
表名：user
字段：
- id: BIGINT, 主键, 自增
- username: VARCHAR(50), 非空, 用户名
- password: VARCHAR(255), 非空, 密码(加密存储)
- nickname: VARCHAR(50), 可空, 用户昵称
- salary: INT, 默认10000, 当前薪资(元/月)
- createTime: DATETIME, 默认CURRENT_TIMESTAMP, 创建时间
- updateTime: DATETIME, 默认CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 更新时间
- isDelete: TINYINT, 默认0, 逻辑删除(0-未删除,1-已删除)

### 要求：
1. 生成完整的建表语句
2. 包含适当的索引
3. 添加表注释和字段注释
4. 使用utf8mb4字符集