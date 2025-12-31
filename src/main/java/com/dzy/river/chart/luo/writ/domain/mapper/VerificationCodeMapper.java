package com.dzy.river.chart.luo.writ.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dzy.river.chart.luo.writ.domain.entity.VerificationCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * 验证码表 Mapper 接口
 *
 * @author zhuzhiwei
 * @since 2025-12-31
 */
@Mapper
public interface VerificationCodeMapper extends BaseMapper<VerificationCode> {

    /**
     * 统计指定时间范围内某个目标的发送次数
     *
     * @param target    目标（手机号或邮箱）
     * @param type      验证码类型
     * @param startTime 开始时间
     * @return 发送次数
     */
    @Select("SELECT COUNT(*) FROM verification_code " +
            "WHERE target = #{target} AND type = #{type} " +
            "AND create_time >= #{startTime}")
    Integer countByTargetAndTypeAndCreateTimeAfter(
            @Param("target") String target,
            @Param("type") String type,
            @Param("startTime") LocalDateTime startTime
    );
}
