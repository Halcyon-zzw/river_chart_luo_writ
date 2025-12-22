package com.dzy.river.chart.luo.writ.aspect;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller层日志切面
 * 统一记录所有Controller接口的入参、出参和耗时
 *
 * @author zhuzhiwei
 * @date 2025/12/22
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

    /**
     * 定义切点：拦截controller包下所有类的所有public方法
     */
    @Pointcut("execution(public * com.dzy.river.chart.luo.writ.controller..*.*(..))")
    public void controllerLog() {
    }

    /**
     * 环绕通知：记录接口日志
     */
    @Around("controllerLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取类名和方法名
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();

        // 获取方法参数（过滤特殊类型）
        Object[] args = point.getArgs();
        List<Object> logArgs = new ArrayList<>();
        for (Object arg : args) {
            // 过滤不需要序列化的参数类型
            if (arg instanceof HttpServletRequest ||
                arg instanceof HttpServletResponse ||
                arg instanceof MultipartFile ||
                arg instanceof MultipartFile[]) {
                // 对于文件类型，记录文件名
                if (arg instanceof MultipartFile) {
                    logArgs.add("MultipartFile: " + ((MultipartFile) arg).getOriginalFilename());
                } else if (arg instanceof MultipartFile[]) {
                    MultipartFile[] files = (MultipartFile[]) arg;
                    List<String> fileNames = new ArrayList<>();
                    for (MultipartFile file : files) {
                        fileNames.add(file.getOriginalFilename());
                    }
                    logArgs.add("MultipartFile[]: " + fileNames);
                } else {
                    logArgs.add(arg.getClass().getSimpleName());
                }
            } else {
                logArgs.add(arg);
            }
        }

        // 记录入参
        log.info("==> [{}#{}] 入参: {}", className, methodName, JSONUtil.toJsonStr(logArgs));

        // 执行方法并计时
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = point.proceed();
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 记录出参和耗时
            log.info("<== [{}#{}] 出参: {}, 耗时: {}ms",
                className, methodName, JSONUtil.toJsonStr(result), costTime);

            return result;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 记录异常
            log.error("<== [{}#{}] 异常: {}, 耗时: {}ms",
                className, methodName, e.getMessage(), costTime, e);

            throw e;
        }
    }
}
