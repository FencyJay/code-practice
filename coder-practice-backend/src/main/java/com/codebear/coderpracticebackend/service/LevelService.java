package com.codebear.coderpracticebackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.codebear.coderpracticebackend.common.PageRequest;
import com.codebear.coderpracticebackend.common.PageResult;
import com.codebear.coderpracticebackend.model.entity.Level;
import com.codebear.coderpracticebackend.model.dto.level.LevelAddRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelQueryRequest;
import com.codebear.coderpracticebackend.model.dto.level.LevelUpdateRequest;

/**
 * 关卡服务接口
 *
 * @author fency
 */
public interface LevelService extends IService<Level> {

    /**
     * 获取关卡列表（分页）- 通用查询方法
     *
     * @param levelQueryRequest 查询请求
     * @return 关卡列表
     */
    PageResult<Level> getLevelList(LevelQueryRequest levelQueryRequest);

    /**
     * 获取关卡列表（分页）- 兼容旧接口
     *
     * @param pageRequest 分页请求
     * @param difficulty  难度
     * @return 关卡列表
     */
    default PageResult<Level> getLevelList(PageRequest pageRequest, String difficulty) {
        LevelQueryRequest levelQueryRequest = new LevelQueryRequest();
        levelQueryRequest.setCurrent(pageRequest.getCurrent());
        levelQueryRequest.setPageSize(pageRequest.getPageSize());
        levelQueryRequest.setDifficulty(difficulty);
        return getLevelList(levelQueryRequest);
    }

    /**
     * 根据ID获取关卡
     *
     * @param levelId 关卡ID
     * @return 关卡
     */
    Level getLevelById(Long levelId);

    /**
     * 生成关卡
     *
     * @param salary 薪资
     * @return 关卡
     */
    Level generateLevel(Integer salary);

    /**
     * 根据方向生成关卡
     *
     * @param salary 薪资
     * @param direction 方向
     * @return 关卡
     */
    Level generateLevel(Integer salary, String direction);

    /**
     * 管理员添加关卡
     *
     * @param levelAddRequest 添加关卡请求
     * @return 关卡ID
     */
    Long addLevel(LevelAddRequest levelAddRequest);

    /**
     * 管理员更新关卡
     *
     * @param levelUpdateRequest 更新关卡请求
     * @return 是否成功
     */
    boolean updateLevel(LevelUpdateRequest levelUpdateRequest);

    /**
     * 管理员删除关卡
     *
     * @param levelId 关卡ID
     * @return 是否成功
     */
    boolean deleteLevel(Long levelId);

    /**
     * 设置精选关卡（更新优先级）
     *
     * @param levelId  关卡ID
     * @param priority 优先级
     * @return 是否成功
     */
    boolean setFeaturedLevel(Long levelId, Integer priority);

    /**
     * 获取所有关卡（按优先级排序）
     *
     * @return 关卡列表
     */
    java.util.List<Level> getAllLevelsOrderByPriority();

    /**
     * 分页查询关卡（支持条件筛选）
     *
     * @param levelQueryRequest 查询请求
     * @return 分页结果
     */
    Page<Level> getLevelsByPage(LevelQueryRequest levelQueryRequest);
}