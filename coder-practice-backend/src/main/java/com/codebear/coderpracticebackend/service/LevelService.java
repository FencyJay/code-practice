package com.codebear.coderpracticebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.Level;

public interface LevelService extends IService<Level> {

    PageResult<Level> getLevelList(PageRequest pageRequest, String difficulty);

    Level getLevelById(Long levelId);

    Level generateLevel(Integer salary);
}