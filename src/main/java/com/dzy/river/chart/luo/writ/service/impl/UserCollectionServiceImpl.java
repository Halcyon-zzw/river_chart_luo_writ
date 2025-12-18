package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.ContentDao;
import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.entity.UserCollection;
import com.dzy.river.chart.luo.writ.domain.dto.UserCollectionDTO;
import com.dzy.river.chart.luo.writ.domain.convert.UserCollectionConvert;
import com.dzy.river.chart.luo.writ.dao.UserCollectionDao;
import com.dzy.river.chart.luo.writ.domain.req.CollectionPageReq;
import com.dzy.river.chart.luo.writ.service.UserCollectionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户收藏表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Service
public class UserCollectionServiceImpl implements UserCollectionService {

    @Autowired
    private UserCollectionDao userCollectionDao;

    @Autowired
    private UserCollectionConvert userCollectionConvert;

    @Autowired
    private ContentDao contentDao;

    @Override
    public UserCollectionDTO getById(Long id) {
        UserCollection userCollection = userCollectionDao.getById(id);
        return userCollection != null ? userCollectionConvert.toUserCollectionDTO(userCollection) : null;
    }

    @Override
    public UserCollectionDTO save(UserCollectionDTO userCollectionDTO) {
        UserCollection userCollection = userCollectionConvert.toUserCollection(userCollectionDTO);
        boolean success = userCollectionDao.save(userCollection);
        return success ? userCollectionConvert.toUserCollectionDTO(userCollection) : null;
    }

    @Override
    public boolean removeById(Long id) {
        return userCollectionDao.removeById(id);
    }

    @Override
    public UserCollectionDTO updateById(Long id, UserCollectionDTO userCollectionDTO) {
        userCollectionDTO.setId(id);
        UserCollection userCollection = userCollectionConvert.toUserCollection(userCollectionDTO);
        boolean success = userCollectionDao.updateById(userCollection);
        return success ? userCollectionConvert.toUserCollectionDTO(userCollection) : null;
    }

    @Override
    public PageResult<UserCollectionDTO> page(CollectionPageReq collectionPageReq) {
        // 1. 先查询符合 contentType 条件的 contentId 列表
        LambdaQueryWrapper<Content> contentQueryWrapper = new LambdaQueryWrapper<>();
        contentQueryWrapper.eq(Content::getContentType, collectionPageReq.getContentType());
        List<Content> contents = contentDao.list(contentQueryWrapper);

        // 2. 如果没有符合条件的内容，返回空结果
        if (CollectionUtils.isEmpty(contents)) {
            return new PageResult<>(new Page<>(collectionPageReq.getPageNum(), collectionPageReq.getPageSize()), List.of());
        }

        // 3. 提取 contentId 列表
        List<Long> contentIds = contents.stream()
                .map(Content::getId)
                .collect(Collectors.toList());

        // 4. 构建收藏查询条件
        LambdaQueryWrapper<UserCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCollection::getUserId, collectionPageReq.getUserId());
        queryWrapper.in(UserCollection::getContentId, contentIds);
        if (StringUtils.hasText(collectionPageReq.getName())) {
            queryWrapper.like(UserCollection::getFolderName, collectionPageReq.getName());
        }
        queryWrapper.orderByDesc(UserCollection::getCreatedTime);

        // 5. 分页查询
        Page<UserCollection> page = userCollectionDao.page(collectionPageReq.convertToPage(), queryWrapper);
        List<UserCollection> userCollections = page.getRecords();

        // 6. 转换为DTO
        List<UserCollectionDTO> userCollectionDTOList = userCollections.stream()
                .map(userCollectionConvert::toUserCollectionDTO)
                .collect(Collectors.toList());

        // 7. 返回分页结果
        return new PageResult<>(page, userCollectionDTOList);
    }

}
