-- 为用户表添加头像字段
ALTER TABLE user ADD COLUMN avatar VARCHAR(512) COMMENT '用户头像URL';

-- 为已存在的用户设置默认头像（可选）
-- UPDATE user SET avatar = '/avatars/avatar1.svg' WHERE avatar IS NULL;