package com.dzy.river.chart.luo.writ.service.impl;

import com.dzy.river.chart.luo.writ.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务实现类
 *
 * @author zhuzhiwei
 * @date 2025/12/18
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};

    @Override
    public String uploadImage(MultipartFile file) {
        // 1. 验证文件
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 2. 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isValidImageFile(originalFilename)) {
            throw new IllegalArgumentException("不支持的图片格式，仅支持 jpg、jpeg、png、gif、bmp、webp");
        }

        // 3. 生成文件名：使用日期+UUID
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 4. 创建完整的文件路径
        String relativePath = dateFolder + "/" + newFileName;
        Path fullPath = Paths.get(uploadPath, relativePath);

        try {
            // 5. 创建目录
            Files.createDirectories(fullPath.getParent());

            // 6. 保存文件
            file.transferTo(fullPath.toFile());

            // 7. 返回访问URL（返回相对路径，前端可通过HTTP访问）
            String accessUrl = urlPrefix + relativePath.replace("\\", "/");
            log.info("文件上传成功，本地路径: {}, 访问URL: {}", fullPath, accessUrl);
            return accessUrl;

        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public List<String> uploadImages(MultipartFile[] files, int maxCount) {
        // 1. 验证文件数组
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 2. 验证上传数量
        if (files.length > maxCount) {
            throw new IllegalArgumentException("单次最多上传 " + maxCount + " 个文件，当前上传了 " + files.length + " 个");
        }

        // 3. 批量上传
        List<String> imageUrls = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                // 跳过空文件
                if (file == null || file.isEmpty()) {
                    log.warn("第 {} 个文件为空，已跳过", i + 1);
                    continue;
                }

                String imageUrl = uploadImage(file);
                imageUrls.add(imageUrl);

            } catch (Exception e) {
                String filename = file != null ? file.getOriginalFilename() : "unknown";
                log.error("第 {} 个文件 [{}] 上传失败: {}", i + 1, filename, e.getMessage());
                failedFiles.add(filename);
            }
        }

        // 4. 检查是否有失败的文件
        if (!failedFiles.isEmpty()) {
            log.warn("部分文件上传失败: {}", failedFiles);
            // 如果所有文件都失败了，抛出异常
            if (imageUrls.isEmpty()) {
                throw new RuntimeException("所有文件上传失败");
            }
        }

        log.info("批量上传完成，成功: {}, 失败: {}", imageUrls.size(), failedFiles.size());
        return imageUrls;
    }

    /**
     * 验证是否为有效的图片文件
     */
    private boolean isValidImageFile(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        for (String extension : ALLOWED_EXTENSIONS) {
            if (lowerCaseFilename.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
