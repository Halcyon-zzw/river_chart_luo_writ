package com.dzy.river.chart.luo.writ.domain.req;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 操作日志表 Req
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "OperationLogReq", description = "操作日志表 Req")
public class OperationLogReq implements Serializable {



}