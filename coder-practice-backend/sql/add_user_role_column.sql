-- 为用户表添加userRole字段，用于区分用户角色
ALTER TABLE `user` ADD COLUMN `userRole` VARCHAR(20) DEFAULT 'user' COMMENT '用户角色（user普通用户，admin管理员）' AFTER `salary`;

-- 更新现有记录的userRole字段为默认值'user'
UPDATE `user` SET `userRole` = 'user' WHERE `userRole` IS NULL;