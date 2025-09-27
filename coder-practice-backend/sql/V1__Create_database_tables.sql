-- 创建数据库
CREATE DATABASE IF NOT EXISTS coder_practice DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE coder_practice;

-- 用户表
CREATE TABLE `user`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`   VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`   VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `nickname`   VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    `avatar`     VARCHAR(255) DEFAULT NULL COMMENT '用户头像',
    `salary`     INT         DEFAULT 10000 COMMENT '当前薪资（单位：元/月）',
    `createTime` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   TINYINT     DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY          `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 关卡表
CREATE TABLE `level`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `levelName`    VARCHAR(200) NOT NULL COMMENT '关卡名称',
    `levelDesc`    TEXT         NOT NULL COMMENT '关卡需求描述',
    `options`      TEXT         NOT NULL COMMENT '关卡选项（JSON格式存储）',
    `difficulty`   VARCHAR(200) NOT NULL COMMENT '难度等级（简单，中等，困难）',
    `targetSalary` INT      DEFAULT 10000 COMMENT '目标薪资范围（用于难度匹配）',
    `createTime`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     TINYINT  DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    KEY            `idx_difficulty` (`difficulty`),
    KEY            `idx_targetSalary` (`targetSalary`),
    KEY            `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关卡表';

-- 用户关卡表：记录用户闯关信息和结果报告
CREATE TABLE `user_level`
(
    `id`             BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `userId`         BIGINT NOT NULL COMMENT '用户ID',
    `levelId`        BIGINT NOT NULL COMMENT '关卡ID',
    `userOptions`    TEXT   NOT NULL COMMENT '用户选择的选项（JSON格式存储）',
    `score`          INT      DEFAULT 0 COMMENT '得分（0-100分）',
    `comment`        TEXT     DEFAULT NULL COMMENT '评价',
    `salaryChange`   INT      DEFAULT 0 COMMENT '薪资变化（正数为加薪，负数为减薪）',
    `suggest`        TEXT     DEFAULT NULL COMMENT '公司投递建议',
    `reason`         TEXT     DEFAULT NULL COMMENT '评分原因',
    `trueOptions`    TEXT     DEFAULT NULL COMMENT '正确选项（JSON格式存储）',
    `standardAnswer` TEXT     DEFAULT NULL COMMENT '标准答案解析',
    `createTime`     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`       TINYINT  DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    PRIMARY KEY (`id`),
    KEY              `idx_userId` (`userId`),
    KEY              `idx_levelId` (`levelId`),
    KEY              `idx_score` (`score`),
    KEY              `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关卡表';