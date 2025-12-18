package com.dzy.river.chart.luo.writ.domain.req;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * <p>
 * 用户收藏表 Req
 * </p>
 *
 * @author zhuzhiwei
 * @since 2025-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "UserCollectionReq", description = "用户收藏表 Req")
public class UserCollectionReq implements Serializable {



}