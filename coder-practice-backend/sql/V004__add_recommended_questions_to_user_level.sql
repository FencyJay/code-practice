-- 为user_level表添加推荐面试题字段
ALTER TABLE user_level ADD COLUMN recommendedQuestions TEXT COMMENT '推荐面试题JSON';

-- 为已有记录设置默认值
UPDATE user_level SET recommended_questions = '[]' WHERE recommended_questions IS NULL;