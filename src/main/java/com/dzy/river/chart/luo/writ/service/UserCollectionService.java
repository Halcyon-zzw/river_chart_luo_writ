package com.dzy.river.chart.luo.writ.service;

import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.domain.dto.UserCollectionDTO;
import com.dzy.river.chart.luo.writ.domain.req.CollectionPageReq;

/**
 * <p>
 * 用户收藏表 服务类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
public interface UserCollectionService {

    /**
     * 根据ID获取用户收藏表
     *
     * @param id 主键ID
     * @return 用户收藏表DTO对象
     */
    UserCollectionDTO getById(Long id);

    /**
     * 保存用户收藏表
     *
     * @param userCollectionDTO 用户收藏表DTO对象
     * @return 用户收藏表DTO对象
     */
    UserCollectionDTO save(UserCollectionDTO userCollectionDTO);

    /**
     * 根据ID删除用户收藏表
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteByContentId(Long id);

    /**
     * 根据ID更新用户收藏表
     *
     * @param id 主键ID
     * @param userCollectionDTO 用户收藏表DTO对象
     * @return 用户收藏表DTO对象
     */
    UserCollectionDTO updateById(Long id, UserCollectionDTO userCollectionDTO);

    /**
     * 分页查询用户收藏列表
     *
     * @param collectionPageReq 分页请求参数
     * @return 分页结果
     */
    PageResult<UserCollectionDTO> page(CollectionPageReq collectionPageReq);

}
