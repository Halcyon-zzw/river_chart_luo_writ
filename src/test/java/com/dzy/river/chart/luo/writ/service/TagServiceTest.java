package com.dzy.river.chart.luo.writ.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dzy.river.chart.luo.writ.dao.TagDao;
import com.dzy.river.chart.luo.writ.domain.convert.TagConvert;
import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.entity.Tag;
import com.dzy.river.chart.luo.writ.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * TagService 单元测试
 *
 * 测试模式：Given-When-Then
 * - Given: 准备测试数据和mock行为
 * - When: 执行被测试的方法
 * - Then: 验证结果和交互
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("标签服务单元测试")
class TagServiceTest {

    @Mock
    private TagDao tagDao;

    @Mock
    private TagConvert tagConvert;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag mockTag;
    private TagDTO mockTagDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        mockTag = new Tag();
        mockTag.setId(1L);
        mockTag.setName("测试标签");
        mockTag.setCreateTime(LocalDateTime.now());
        mockTag.setUpdateTime(LocalDateTime.now());

        mockTagDTO = new TagDTO();
        mockTagDTO.setId(1L);
        mockTagDTO.setName("测试标签");
    }

    // ==================== 查询测试 ====================

    @Test
    @DisplayName("查询成功 - 根据ID获取标签")
    void getById_shouldReturnTag_whenTagExists() {
        // Given: 准备mock数据
        Long tagId = 1L;
        when(tagDao.getById(tagId)).thenReturn(mockTag);
        when(tagConvert.toTagDTO(mockTag)).thenReturn(mockTagDTO);

        // When: 执行查询
        TagDTO result = tagService.getById(tagId);

        // Then: 验证结果
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("测试标签");

        // 验证方法调用
        verify(tagDao, times(1)).getById(tagId);
        verify(tagConvert, times(1)).toTagDTO(mockTag);
    }

    @Test
    @DisplayName("查询失败 - 标签不存在时返回null")
    void getById_shouldReturnNull_whenTagNotExists() {
        // Given
        Long tagId = 999L;
        when(tagDao.getById(tagId)).thenReturn(null);

        // When
        TagDTO result = tagService.getById(tagId);

        // Then
        assertThat(result).isNull();
        verify(tagDao, times(1)).getById(tagId);
        verify(tagConvert, never()).toTagDTO(any());
    }

    @Test
    @DisplayName("查询列表 - 根据名称模糊查询")
    void queryByName_shouldReturnList_whenNameMatches() {
        // Given
        String name = "测试";
        List<Tag> mockTags = Arrays.asList(mockTag);
        List<TagDTO> mockTagDTOs = Arrays.asList(mockTagDTO);

        when(tagDao.list(any(LambdaQueryWrapper.class))).thenReturn(mockTags);
        when(tagConvert.toTagDTOList(mockTags)).thenReturn(mockTagDTOs);

        // When
        List<TagDTO> result = tagService.queryByName(name);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("测试");

        verify(tagDao, times(1)).list(any(LambdaQueryWrapper.class));
        verify(tagConvert, times(1)).toTagDTOList(mockTags);
    }

    // ==================== 新增测试 ====================

    @Test
    @DisplayName("新增成功 - 保存标签")
    void save_shouldReturnSavedTag_whenSaveSuccessful() {
        // Given
        TagDTO inputDTO = new TagDTO();
        inputDTO.setName("新标签");

        Tag inputTag = new Tag();
        inputTag.setName("新标签");

        when(tagConvert.toTag(inputDTO)).thenReturn(inputTag);
        when(tagDao.save(inputTag)).thenReturn(true);
        when(tagConvert.toTagDTO(inputTag)).thenReturn(mockTagDTO);

        // When
        TagDTO result = tagService.save(inputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("测试标签");

        verify(tagConvert, times(1)).toTag(inputDTO);
        verify(tagDao, times(1)).save(inputTag);
        verify(tagConvert, times(1)).toTagDTO(inputTag);
    }

    @Test
    @DisplayName("新增失败 - 保存失败时返回null")
    void save_shouldReturnNull_whenSaveFailed() {
        // Given
        TagDTO inputDTO = new TagDTO();
        inputDTO.setName("新标签");

        Tag inputTag = new Tag();
        when(tagConvert.toTag(inputDTO)).thenReturn(inputTag);
        when(tagDao.save(inputTag)).thenReturn(false);

        // When
        TagDTO result = tagService.save(inputDTO);

        // Then
        assertThat(result).isNull();
        verify(tagDao, times(1)).save(inputTag);
        verify(tagConvert, never()).toTagDTO(any());
    }

    @Test
    @DisplayName("新增验证 - 验证保存的数据内容")
    void save_shouldSaveCorrectData() {
        // Given
        TagDTO inputDTO = new TagDTO();
        inputDTO.setName("新标签");

        ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
        when(tagConvert.toTag(inputDTO)).thenReturn(mockTag);
        when(tagDao.save(any(Tag.class))).thenReturn(true);
        when(tagConvert.toTagDTO(any(Tag.class))).thenReturn(mockTagDTO);

        // When
        tagService.save(inputDTO);

        // Then: 捕获并验证保存的对象
        verify(tagDao).save(tagCaptor.capture());
        Tag savedTag = tagCaptor.getValue();
        assertThat(savedTag).isNotNull();
    }

    // ==================== 修改测试 ====================

    @Test
    @DisplayName("修改成功 - 更新标签")
    void updateById_shouldReturnUpdatedTag_whenUpdateSuccessful() {
        // Given
        Long tagId = 1L;
        TagDTO updateDTO = new TagDTO();
        updateDTO.setName("更新后的标签");

        Tag updateTag = new Tag();
        updateTag.setId(tagId);
        updateTag.setName("更新后的标签");

        when(tagConvert.toTag(updateDTO)).thenReturn(updateTag);
        when(tagDao.updateById(updateTag)).thenReturn(true);
        when(tagConvert.toTagDTO(updateTag)).thenReturn(mockTagDTO);

        // When
        TagDTO result = tagService.updateById(tagId, updateDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(updateDTO.getId()).isEqualTo(tagId); // 验证ID被设置

        verify(tagConvert, times(1)).toTag(updateDTO);
        verify(tagDao, times(1)).updateById(updateTag);
        verify(tagConvert, times(1)).toTagDTO(updateTag);
    }

    @Test
    @DisplayName("修改失败 - 更新失败时返回null")
    void updateById_shouldReturnNull_whenUpdateFailed() {
        // Given
        Long tagId = 1L;
        TagDTO updateDTO = new TagDTO();
        updateDTO.setName("更新后的标签");

        Tag updateTag = new Tag();
        when(tagConvert.toTag(updateDTO)).thenReturn(updateTag);
        when(tagDao.updateById(updateTag)).thenReturn(false);

        // When
        TagDTO result = tagService.updateById(tagId, updateDTO);

        // Then
        assertThat(result).isNull();
        verify(tagDao, times(1)).updateById(updateTag);
        verify(tagConvert, never()).toTagDTO(any());
    }

    // ==================== 删除测试 ====================

    @Test
    @DisplayName("删除成功 - 根据ID删除标签")
    void removeById_shouldReturnTrue_whenDeleteSuccessful() {
        // Given
        Long tagId = 1L;
        when(tagDao.removeById(tagId)).thenReturn(true);

        // When
        boolean result = tagService.removeById(tagId);

        // Then
        assertThat(result).isTrue();
        verify(tagDao, times(1)).removeById(tagId);
    }

    @Test
    @DisplayName("删除失败 - 标签不存在")
    void removeById_shouldReturnFalse_whenTagNotExists() {
        // Given
        Long tagId = 999L;
        when(tagDao.removeById(tagId)).thenReturn(false);

        // When
        boolean result = tagService.removeById(tagId);

        // Then
        assertThat(result).isFalse();
        verify(tagDao, times(1)).removeById(tagId);
    }

    // ==================== 边界条件测试 ====================

    @Test
    @DisplayName("边界测试 - 名称为null时查询所有")
    void queryByName_shouldReturnAll_whenNameIsNull() {
        // Given
        List<Tag> allTags = Arrays.asList(mockTag);
        List<TagDTO> allTagDTOs = Arrays.asList(mockTagDTO);

        when(tagDao.list(any(LambdaQueryWrapper.class))).thenReturn(allTags);
        when(tagConvert.toTagDTOList(allTags)).thenReturn(allTagDTOs);

        // When
        List<TagDTO> result = tagService.queryByName(null);

        // Then
        assertThat(result).isNotEmpty();
        verify(tagDao, times(1)).list(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("边界测试 - 名称为空字符串时查询所有")
    void queryByName_shouldReturnAll_whenNameIsEmpty() {
        // Given
        List<Tag> allTags = Arrays.asList(mockTag);
        List<TagDTO> allTagDTOs = Arrays.asList(mockTagDTO);

        when(tagDao.list(any(LambdaQueryWrapper.class))).thenReturn(allTags);
        when(tagConvert.toTagDTOList(allTags)).thenReturn(allTagDTOs);

        // When
        List<TagDTO> result = tagService.queryByName("");

        // Then
        assertThat(result).isNotEmpty();
        verify(tagDao, times(1)).list(any(LambdaQueryWrapper.class));
    }
}
