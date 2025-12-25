package com.dzy.river.chart.luo.writ.controller;

import com.dzy.river.chart.luo.writ.domain.dto.TagDTO;
import com.dzy.river.chart.luo.writ.domain.req.BatchLinkContentTagReq;
import com.dzy.river.chart.luo.writ.service.ContentTagService;
import com.dzy.river.chart.luo.writ.service.MainCategoryTagService;
import com.dzy.river.chart.luo.writ.service.SubCategoryTagService;
import com.dzy.river.chart.luo.writ.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TagController 集成测试
 *
 * 使用 @SpringBootTest 和 @AutoConfigureMockMvc 进行完整的集成测试
 * 使用 @MockBean 模拟服务层
 * 使用 MockMvc 模拟HTTP请求
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayName("标签控制器集成测试")
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @MockBean
    private MainCategoryTagService mainCategoryTagService;

    @MockBean
    private SubCategoryTagService subCategoryTagService;

    @MockBean
    private ContentTagService contentTagService;

    private TagDTO mockTagDTO;

    @BeforeEach
    void setUp() {
        mockTagDTO = new TagDTO();
        mockTagDTO.setId(1L);
        mockTagDTO.setName("测试标签");
    }

    // ==================== 查询测试 ====================

    @Test
    @DisplayName("GET /tag/{id} - 查询成功")
    void getTag_shouldReturnTag_whenTagExists() throws Exception {
        // Given
        Long tagId = 1L;
        when(tagService.getById(tagId)).thenReturn(mockTagDTO);

        // When & Then
        mockMvc.perform(get("/tag/{id}", tagId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("测试标签"));

        verify(tagService, times(1)).getById(tagId);
    }

    @Test
    @DisplayName("GET /tag/{id} - 查询失败，标签不存在")
    void getTag_shouldReturn404_whenTagNotExists() throws Exception {
        // Given
        Long tagId = 999L;
        when(tagService.getById(tagId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/tag/{id}", tagId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getById(tagId);
    }

    @Test
    @DisplayName("GET /tag/query - 查询标签列表")
    void query_shouldReturnTagList_whenNameMatches() throws Exception {
        // Given
        String name = "测试";
        List<TagDTO> tags = Arrays.asList(mockTagDTO);
        when(tagService.queryByName(name)).thenReturn(tags);

        // When & Then
        mockMvc.perform(get("/tag/query")
                        .param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("测试标签"));

        verify(tagService, times(1)).queryByName(name);
    }

    @Test
    @DisplayName("GET /tag/query - 查询所有标签（无参数）")
    void query_shouldReturnAllTags_whenNoParameter() throws Exception {
        // Given
        List<TagDTO> tags = Arrays.asList(mockTagDTO);
        when(tagService.queryByName(null)).thenReturn(tags);

        // When & Then
        mockMvc.perform(get("/tag/query"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());

        verify(tagService, times(1)).queryByName(null);
    }

    // ==================== 新增测试 ====================

    @Test
    @DisplayName("POST /tag/create - 新增成功")
    void createTag_shouldReturnCreatedTag_whenValid() throws Exception {
        // Given
        TagDTO inputDTO = new TagDTO();
        inputDTO.setName("新标签");

        when(tagService.save(any(TagDTO.class))).thenReturn(mockTagDTO);

        // When & Then
        mockMvc.perform(post("/tag/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("创建成功"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("测试标签"));

        verify(tagService, times(1)).save(any(TagDTO.class));
    }

    @Test
    @DisplayName("POST /tag/create - 新增失败，参数校验失败")
    void createTag_shouldReturn400_whenInvalidData() throws Exception {
        // Given: 空对象，缺少必填字段
        TagDTO invalidDTO = new TagDTO();

        // When & Then
        mockMvc.perform(post("/tag/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(tagService, never()).save(any());
    }

    // ==================== 修改测试 ====================

    @Test
    @DisplayName("PUT /tag/{id} - 修改成功")
    void updateById_shouldReturnUpdatedTag_whenValid() throws Exception {
        // Given
        Long tagId = 1L;
        TagDTO updateDTO = new TagDTO();
        updateDTO.setName("更新后的标签");

        TagDTO updatedDTO = new TagDTO();
        updatedDTO.setId(tagId);
        updatedDTO.setName("更新后的标签");

        when(tagService.updateById(eq(tagId), any(TagDTO.class))).thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/tag/{id}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("更新成功"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("更新后的标签"));

        verify(tagService, times(1)).updateById(eq(tagId), any(TagDTO.class));
    }

    @Test
    @DisplayName("PUT /tag/{id} - 修改失败，标签不存在")
    void updateById_shouldReturn404_whenTagNotExists() throws Exception {
        // Given
        Long tagId = 999L;
        TagDTO updateDTO = new TagDTO();
        updateDTO.setName("更新后的标签");

        when(tagService.updateById(eq(tagId), any(TagDTO.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/tag/{id}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).updateById(eq(tagId), any(TagDTO.class));
    }

    // ==================== 删除测试 ====================

    @Test
    @DisplayName("DELETE /tag/{id} - 删除成功")
    void deleteById_shouldReturnTrue_whenTagExists() throws Exception {
        // Given
        Long tagId = 1L;
        when(tagService.removeById(tagId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/tag/{id}", tagId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("删除成功"))
                .andExpect(jsonPath("$.data").value(true));

        verify(tagService, times(1)).removeById(tagId);
    }

    @Test
    @DisplayName("DELETE /tag/{id} - 删除失败，标签不存在")
    void deleteById_shouldReturn404_whenTagNotExists() throws Exception {
        // Given
        Long tagId = 999L;
        when(tagService.removeById(tagId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/tag/{id}", tagId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).removeById(tagId);
    }

    // ==================== 批量关联测试 ====================

    @Test
    @DisplayName("POST /tag/batch-link-content - 批量关联成功")
    void batchLinkContentTags_shouldReturnCount_whenValid() throws Exception {
        // Given
        BatchLinkContentTagReq req = new BatchLinkContentTagReq();
        req.setContentId(1L);
        req.setTagIds(Arrays.asList(1L, 2L, 3L));

        when(contentTagService.batchLinkTags(1L, req.getTagIds())).thenReturn(3);

        // When & Then
        mockMvc.perform(post("/tag/batch-link-content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("批量关联成功"))
                .andExpect(jsonPath("$.data").value(3));

        verify(contentTagService, times(1)).batchLinkTags(1L, req.getTagIds());
    }

    @Test
    @DisplayName("POST /tag/batch-link-content - 参数校验失败（contentId为空）")
    void batchLinkContentTags_shouldReturn400_whenContentIdIsNull() throws Exception {
        // Given
        BatchLinkContentTagReq req = new BatchLinkContentTagReq();
        req.setContentId(null); // 空的contentId
        req.setTagIds(Arrays.asList(1L, 2L));

        // When & Then
        mockMvc.perform(post("/tag/batch-link-content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(contentTagService, never()).batchLinkTags(any(), any());
    }

    @Test
    @DisplayName("POST /tag/batch-link-content - 参数校验失败（tagIds为空）")
    void batchLinkContentTags_shouldReturn400_whenTagIdsIsEmpty() throws Exception {
        // Given
        BatchLinkContentTagReq req = new BatchLinkContentTagReq();
        req.setContentId(1L);
        req.setTagIds(Arrays.asList()); // 空列表

        // When & Then
        mockMvc.perform(post("/tag/batch-link-content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(contentTagService, never()).batchLinkTags(any(), any());
    }

    // ==================== 异常场景测试 ====================

    @Test
    @DisplayName("异常处理 - 内部服务异常")
    void handleException_whenServiceThrowsException() throws Exception {
        // Given
        Long tagId = 1L;
        when(tagService.getById(tagId)).thenThrow(new RuntimeException("数据库连接失败"));

        // When & Then
        mockMvc.perform(get("/tag/{id}", tagId))
                .andDo(print())
                .andExpect(status().is5xxServerError());

        verify(tagService, times(1)).getById(tagId);
    }
}
