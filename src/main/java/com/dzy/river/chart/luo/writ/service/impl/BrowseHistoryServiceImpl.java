package com.dzy.river.chart.luo.writ.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.BrowseHistoryDao;
import com.dzy.river.chart.luo.writ.dao.ContentDao;
import com.dzy.river.chart.luo.writ.domain.convert.BrowseHistoryConvert;
import com.dzy.river.chart.luo.writ.domain.convert.ContentConvert;
import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.dto.ContentDTO;
import com.dzy.river.chart.luo.writ.domain.dto.TimeRangeTypeDTO;
import com.dzy.river.chart.luo.writ.domain.entity.BrowseHistory;
import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.enums.TimeRangeTypeEnum;
import com.dzy.river.chart.luo.writ.domain.req.BrowseHistoryPageReq;
import com.dzy.river.chart.luo.writ.domain.req.ClearReq;
import com.dzy.river.chart.luo.writ.mapper.BrowseHistoryMapper;
import com.dzy.river.chart.luo.writ.mapper.ContentMapper;
import com.dzy.river.chart.luo.writ.service.BrowseHistoryService;
import com.dzy.river.chart.luo.writ.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 浏览历史表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
@Slf4j
@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {

    @Autowired
    private BrowseHistoryDao browseHistoryDao;

    @Autowired
    private BrowseHistoryMapper browseHistoryMapper;

    @Autowired
    private BrowseHistoryConvert browseHistoryConvert;

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private ContentConvert contentConvert;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean recordBrowse(Long contentId, Long userId) {
        // 1. 查询内容的类型
        Content content = contentDao.getById(contentId);
        String contentType = (content != null) ? content.getContentType() : null;

        // 2. 查询是否已存在浏览记录（包括已删除的记录）
        BrowseHistory existingRecord = browseHistoryDao.getBaseMapper()
                .selectByContentIdAndUserId(contentId, userId);

        if (existingRecord == null) {
            // 3. 如果记录不存在，尝试插入新记录
            try {
                BrowseHistory newRecord = new BrowseHistory();
                newRecord.setContentId(contentId);
                newRecord.setContentType(contentType);
                newRecord.setUserId(userId);
                newRecord.setBrowseCount(1);
                newRecord.setLastBrowseTime(LocalDateTime.now());
                browseHistoryDao.save(newRecord);
                return true;
            } catch (DuplicateKeyException e) {
                // 4. 插入失败（唯一约束冲突），说明存在已删除的记录，重新查询并更新
                log.warn("Insert browse history failed due to duplicate key, retry with update. contentId={}, userId={}", contentId, userId);
                existingRecord = browseHistoryDao.getBaseMapper()
                        .selectByContentIdAndUserId(contentId, userId);
                if (existingRecord == null) {
                    log.error("Failed to find existing browse history record after duplicate key exception. contentId={}, userId={}", contentId, userId);
                    throw new RuntimeException("记录浏览历史失败");
                }
            }
        }

        // 5. 如果记录已存在，使用 UpdateWrapper 更新（绕过 @TableLogic 限制）
        LambdaUpdateWrapper<BrowseHistory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BrowseHistory::getId, existingRecord.getId());

        // 更新最后浏览时间和内容类型
        updateWrapper.set(BrowseHistory::getLastBrowseTime, LocalDateTime.now());
        updateWrapper.set(BrowseHistory::getContentType, contentType);

        if (existingRecord.getIsDeleted() == 1) {
            // 5.1 如果记录已被逻辑删除，则恢复并重置计数
            updateWrapper.set(BrowseHistory::getIsDeleted, 0);
            updateWrapper.set(BrowseHistory::getBrowseCount, 1);
        } else {
            // 5.2 如果记录未删除，则增加浏览次数
            int newCount = existingRecord.getBrowseCount() + 1;
            updateWrapper.set(BrowseHistory::getBrowseCount, newCount);
        }

        // 执行更新
        browseHistoryDao.update(null, updateWrapper);
        return true;
    }

    @Override
    public BrowseHistoryDTO getById(Long id) {
        BrowseHistory browseHistory = browseHistoryDao.getById(id);
        return browseHistory != null ? browseHistoryConvert.toBrowseHistoryDTO(browseHistory) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        return browseHistoryDao.removeById(id);
    }

    @Override
    public PageResult<BrowseHistoryDTO> page(BrowseHistoryPageReq pageReq) {

        setTimeRange(pageReq);

        // 方案A：分步查询，不使用 JOIN
        // 1. 如果有 contentTitle 或 contentType（只在需要从 content 表过滤时），先查询 content_id 列表
        if (pageReq.getContentTitle() != null && !pageReq.getContentTitle().trim().isEmpty()) {
            List<Long> contentIds = contentMapper.selectIdsByTitleAndType(
                    pageReq.getContentTitle(),
                    pageReq.getContentType()
            );

            // 如果没有匹配的内容，直接返回空结果
            if (contentIds.isEmpty()) {
                Page<BrowseHistoryDTO> emptyPage = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
                emptyPage.setRecords(new ArrayList<>());
                emptyPage.setTotal(0);
                return new PageResult<>(emptyPage);
            }

            pageReq.setContentIds(contentIds);
        }

        // 2. 查询 browse_history 表，获取分页数据（不使用 JOIN）
        Page<BrowseHistory> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        IPage<BrowseHistory> browseHistoryPage = browseHistoryMapper.selectPageWithoutJoin(page, pageReq);

        log.info("BrowseHistoryServiceImpl.page: {}", JSONUtil.toJsonStr(browseHistoryPage));

        // 如果没有浏览历史，直接返回空结果
        if (browseHistoryPage.getRecords().isEmpty()) {
            Page<BrowseHistoryDTO> emptyPage = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
            emptyPage.setRecords(new ArrayList<>());
            emptyPage.setTotal(0);
            return new PageResult<>(emptyPage);
        }

        // 3. 提取所有 content_id，批量查询 content 表
        List<Long> contentIds = browseHistoryPage.getRecords().stream()
                .map(BrowseHistory::getContentId)
                .distinct()
                .collect(Collectors.toList());

        List<Content> contents = contentDao.listByIds(contentIds);

        // 4. 使用 ContentConvert 将 Content → ContentDTO（这样 imageUrlList 会被正确填充）
        List<ContentDTO> contentDTOs = contentConvert.toContentDTOList(contents);

        // 创建 contentId -> ContentDTO 的映射
        Map<Long, ContentDTO> contentMap = contentDTOs.stream()
                .collect(Collectors.toMap(ContentDTO::getId, dto -> dto));

        // 5. 在内存中组装 BrowseHistoryDTO
        List<BrowseHistoryDTO> dtoList = browseHistoryPage.getRecords().stream()
                .map(browseHistory -> {
                    BrowseHistoryDTO dto = browseHistoryConvert.toBrowseHistoryDTO(browseHistory);
                    // 设置 contentDTO（即使 content 已被删除，也设置 null）
                    ContentDTO contentDTO = contentMap.get(browseHistory.getContentId());
                    dto.setContentDTO(contentDTO);
                    return dto;
                })
                .collect(Collectors.toList());

        // 6. 构建分页结果
        Page<BrowseHistoryDTO> resultPage = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        resultPage.setRecords(dtoList);
        resultPage.setTotal(browseHistoryPage.getTotal());

        return new PageResult<>(resultPage);
    }

    private void setTimeRange(BrowseHistoryPageReq pageReq) {

        TimeRangeTypeEnum value = TimeRangeTypeEnum.getByCode(pageReq.getTimeRangeType());
        if (value == null) {
            return;
        }

        LocalDateTime startTime = null;
        LocalDateTime now = LocalDateTime.now();

        switch (value) {
            case ALL -> {
                return;
            }
            case TODAY -> {
                startTime = now.toLocalDate().atStartOfDay();
            }
            case THREE_DAYS -> {
                startTime = now.minusDays(2).toLocalDate().atStartOfDay();
            }
            case SEVEN_DAYS -> {
                startTime = now.minusDays(6).toLocalDate().atStartOfDay();
            }
            case ONE_MONTH -> {
                startTime = now.minusDays(29).toLocalDate().atStartOfDay();
            }
        }
        pageReq.setStartTime(startTime);
        pageReq.setEndTime(now);
    }

    @Override
    public Integer getContentBrowseCount(Long contentId) {
        LambdaQueryWrapper<BrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BrowseHistory::getContentId, contentId);
        queryWrapper.select(BrowseHistory::getBrowseCount);

        List<BrowseHistory> records = browseHistoryDao.list(queryWrapper);

        return records.stream()
                .mapToInt(BrowseHistory::getBrowseCount)
                .sum();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearByUserId(ClearReq clearReq) {
        // 构建更新条件：使用 LambdaUpdateWrapper 同时指定 SET 和 WHERE 子句
        LambdaUpdateWrapper<BrowseHistory> updateWrapper = new LambdaUpdateWrapper<>();

        // SET 子句：设置 is_deleted = 1
        updateWrapper.set(BrowseHistory::getIsDeleted, 1);

        // WHERE 子句：根据 userId 和未删除状态过滤
        Long userId = UserUtil.getUserId();
        updateWrapper.eq(BrowseHistory::getUserId, userId);
        updateWrapper.eq(BrowseHistory::getIsDeleted, 0);

        String contentType = clearReq.getContentType();
        // 如果指定了内容类型，则添加类型过滤条件
        if (contentType != null && !contentType.trim().isEmpty()) {
            updateWrapper.eq(BrowseHistory::getContentType, contentType);
        }

        // 先查询符合条件的记录数
        LambdaQueryWrapper<BrowseHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BrowseHistory::getUserId, userId);
        queryWrapper.eq(BrowseHistory::getIsDeleted, 0);
        if (contentType != null && !contentType.trim().isEmpty()) {
            queryWrapper.eq(BrowseHistory::getContentType, contentType);
        }
        long count = browseHistoryDao.count(queryWrapper);

        // 执行更新
        if (count > 0) {
            browseHistoryDao.update(null, updateWrapper);
        }

        return (int) count;
    }

    @Override
    public List<TimeRangeTypeDTO> listTimeRangeTypeList() {



        List<TimeRangeTypeDTO> result = new ArrayList<>();

        // 通常ALL不需要在过滤器中显示
        for (TimeRangeTypeEnum value : TimeRangeTypeEnum.values()) {
            TimeRangeTypeDTO dto = new TimeRangeTypeDTO();
            dto.setCode(value.getCode());
            dto.setDesc(value.getDesc());

            result.add(dto);
        }
        return result;
    }
}
