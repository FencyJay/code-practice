package com.codebear.coderpracticebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codebear.coderpracticebackend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}