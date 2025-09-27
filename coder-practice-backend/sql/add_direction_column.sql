-- 为关卡表添加岗位方向字段
ALTER TABLE `level`
ADD COLUMN `direction` VARCHAR(50) DEFAULT NULL COMMENT '岗位方向（前端开发、后端开发、Java后端开发、软件测试、AI算法、Unity游戏开发、网络运维、小程序等）'
AFTER `difficulty`;

-- 为现有记录设置默认值（可选）
-- UPDATE `level` SET `direction` = '全栈开发' WHERE `direction` IS NULL;

-- 添加索引以提高查询性能
ALTER TABLE `level`
ADD INDEX `idx_direction` (`direction`);