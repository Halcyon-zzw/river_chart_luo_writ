package com.dzy.river.chart.luo.writ.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传服务接口
 *
 * @author zhuzhiwei
 * @date 2025/12/18
 */
public interface FileUploadService {

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 图片访问URL
     */
    String uploadImage(MultipartFile file);

    /**
     * 批量上传图片
     *
     * @param files 图片文件列表
     * @param maxCount 最大上传数量
     * @return 图片访问URL列表
     */
    List<String> uploadImages(MultipartFile[] files, int maxCount);
}
