package com.dzy.river.chart.luo.writ.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.common.PageResult;
import com.dzy.river.chart.luo.writ.dao.BrowseHistoryDao;
import com.dzy.river.chart.luo.writ.dao.ContentDao;
import com.dzy.river.chart.luo.writ.domain.convert.BrowseHistoryConvert;
import com.dzy.river.chart.luo.writ.domain.dto.BrowseHistoryDTO;
import com.dzy.river.chart.luo.writ.domain.entity.BrowseHistory;
import com.dzy.river.chart.luo.writ.domain.entity.Content;
import com.dzy.river.chart.luo.writ.domain.req.BrowseHistoryPageReq;
import com.dzy.river.chart.luo.writ.mapper.BrowseHistoryMapper;
import com.dzy.river.chart.luo.writ.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 浏览历史表 服务实现类
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-26
 */
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BrowseHistoryDTO recordBrowse(Long contentId, Long userId) {
        // 1. 查询内容的类型
        Content content = contentDao.getById(contentId);
        String contentType = (content != null) ? content.getContentType() : null;

        // 2. 查询是否已存在浏览记录（包括已删除的记录）
        BrowseHistory existingRecord = browseHistoryDao.getBaseMapper()
                .selectByContentIdAndUserId(contentId, userId);

        if (existingRecord != null) {
            // 3. 如果记录已存在
            if (existingRecord.getIsDeleted() == 1) {
                // 3.1 如果记录已被逻辑删除，则恢复并重置计数
                existingRecord.setIsDeleted((byte) 0);
                existingRecord.setBrowseCount(1);
            } else {
                // 3.2 如果记录未删除，则增加浏览次数
                existingRecord.setBrowseCount(existingRecord.getBrowseCount() + 1);
            }
            // 更新最后浏览时间和内容类型（内容类型可能改变）
            existingRecord.setLastBrowseTime(LocalDateTime.now());
            existingRecord.setContentType(contentType);
            browseHistoryDao.updateById(existingRecord);
            return browseHistoryConvert.toBrowseHistoryDTO(existingRecord);
        } else {
            // 4. 如果记录不存在，则插入新记录
            BrowseHistory newRecord = new BrowseHistory();
            newRecord.setContentId(contentId);
            newRecord.setContentType(contentType);
            newRecord.setUserId(userId);
            newRecord.setBrowseCount(1);
            newRecord.setLastBrowseTime(LocalDateTime.now());
            browseHistoryDao.save(newRecord);
            return browseHistoryConvert.toBrowseHistoryDTO(newRecord);
        }
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
        // 使用新的关联查询方法（包含内容标题、时间范围过滤、模糊查询）
        Page<BrowseHistoryDTO> page = new Page<>(pageReq.getPageNum(), pageReq.getPageSize());
        IPage<BrowseHistoryDTO> resultPage = browseHistoryMapper.selectPageWithContentTitle(page, pageReq);

        return new PageResult<>(resultPage);
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
    public Integer clearByUserId(Long userId, String contentType) {
        // 构建更新条件：使用 LambdaUpdateWrapper 同时指定 SET 和 WHERE 子句
        LambdaUpdateWrapper<BrowseHistory> updateWrapper = new LambdaUpdateWrapper<>();

        // SET 子句：设置 is_deleted = 1
        updateWrapper.set(BrowseHistory::getIsDeleted, 1);

        // WHERE 子句：根据 userId 和未删除状态过滤
        updateWrapper.eq(BrowseHistory::getUserId, userId);
        updateWrapper.eq(BrowseHistory::getIsDeleted, 0);

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
}
