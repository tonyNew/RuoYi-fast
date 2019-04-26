package com.sinoiov.framework.aspectj;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sinoiov.common.utils.StringUtils;
import com.sinoiov.common.utils.security.ShiroUtils;
import com.sinoiov.framework.aspectj.lang.annotation.DataScope;
import com.sinoiov.framework.web.domain.BaseEntity;
import com.sinoiov.project.system.role.domain.Role;
import com.sinoiov.project.system.user.domain.User;

/**
 * 数据过滤处理
 * 
 * @author tony
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";
    /**
     * 数据分组规则-按子系统分组
     */
    public static final int DATA_SCOPE_GROUP_RULE_SUBSYSTEM = 1;
    /**
     * 自定数据权限-按部门分组
     */
    public static final int DATA_SCOPE_GROUP_RULE_DEPT = 2;

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.sinoiov.framework.aspectj.lang.annotation.DataScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
        // 获取当前的用户
        User currentUser = ShiroUtils.getSysUser();
        if (currentUser != null)
        {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin())
            {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.tableAlias(),controllerDataScope.groupRule());
            }
        }
    }

    /**
     * 数据范围过滤
     * 
     * @param da 部门表别名
     * @return 标准连接条件对象
     */
    public static void dataScopeFilter(JoinPoint joinPoint, User user, String alias,int groupRule)
    {
        StringBuilder sqlString = new StringBuilder();

        for (Role role : user.getRoles())
        {
            String dataScope = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope))
            {
                sqlString = new StringBuilder();
                break;
            }
            else if (DATA_SCOPE_CUSTOM.equals(dataScope))
            {
            	if(groupRule==DATA_SCOPE_GROUP_RULE_DEPT) {
            		sqlString.append(StringUtils.format(
            				" OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", alias,
            				role.getRoleId()));
            	}else if(groupRule==DATA_SCOPE_GROUP_RULE_SUBSYSTEM) {
            		sqlString.append(StringUtils.format(
            				" OR {}.id IN ( SELECT group_id FROM sys_role_data_group WHERE role_id = {} ) ", alias,
            				role.getRoleId()));
            	}
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}
