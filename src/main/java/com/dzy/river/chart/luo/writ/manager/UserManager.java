package com.dzy.river.chart.luo.writ.manager;

import com.dzy.river.chart.luo.writ.domain.entity.User;
import com.dzy.river.chart.luo.writ.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author zhuzhiwei
 * @date 2026/1/5 17:34
 */
@Slf4j
@Component
public class UserManager {


    public void checkUser(User user) {
        // 4. 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            log.warn("Login failed: user is disabled, userId={}", user.getId());
            throw new BusinessException("用户已被禁用");
        }
        if (user.getStatus() == 2) {
            log.warn("Login failed: user is locked, userId={}", user.getId());
            throw new BusinessException("用户已被锁定");
        }
    }
}
