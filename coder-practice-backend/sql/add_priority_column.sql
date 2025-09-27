-- 为关卡表添加priority字段，用于设置精选关卡优先级
ALTER TABLE `level` ADD COLUMN `priority` INT DEFAULT 0 COMMENT '优先级（数值越大优先级越高，0为普通关卡）' AFTER `levelName`;

-- 更新现有记录的priority字段为默认值0
UPDATE `level` SET `priority` = 0 WHERE `priority` IS NULL;