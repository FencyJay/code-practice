package com.codebear.coderpracticebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codebear.coderpracticebackend.model.entity.Level;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LevelMapper extends BaseMapper<Level> {
}