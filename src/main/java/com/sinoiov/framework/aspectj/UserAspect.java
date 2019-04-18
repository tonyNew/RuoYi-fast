package com.sinoiov.framework.aspectj;

import java.lang.reflect.Parameter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sinoiov.common.domain.BaseEntity;

/**
 * 
 * 作    者： niuyi@sinoiov.com
 * 创建于：2019年4月17日
 * 描    述：更新操作人信息及时间
 *
 */
@Aspect
@Component
public class UserAspect
{
    private static final Logger log = LoggerFactory.getLogger(UserAspect.class);

    // 配置织入点
    @Pointcut("target(com.sinoiov.common.service.IService)")
    public void pointCut(){}
    /**
     * 前置通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "pointCut()")
    public void doBefore(JoinPoint point)
    {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        methodSignature = (MethodSignature) signature;
        System.out.println(methodSignature);
        Object target = point.getTarget();
        String methodName = methodSignature.getName();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        System.out.println(methodName);
        if("add".equals(methodName)||"detail".equals(methodName)||"add".equals(methodName)) {
        	if(parameters!=null&&parameters.length==1) {
        		Parameter parameter = parameters[0];
        	}
        };
    }

}
