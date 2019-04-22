package com.sinoiov.framework.web.exception;

import java.util.List;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sinoiov.common.domain.Result;
import com.sinoiov.common.exception.DemoModeException;
import com.sinoiov.common.utils.StringUtils;
import com.sinoiov.common.utils.security.PermissionUtils;
import com.sinoiov.framework.web.domain.AjaxResult;
import com.sinoiov.utils.ResultUtils;

/**
 * 自定义异常处理器
 * 
 * @author tony
 */
@RestControllerAdvice
public class DefaultExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    
    /**
     * 权限校验失败
     */
    @ExceptionHandler(AuthorizationException.class)
    public AjaxResult handleAuthorizationException(AuthorizationException e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error(PermissionUtils.getMsg(e.getMessage()));
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public AjaxResult handleException(HttpRequestMethodNotSupportedException e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult notFount(RuntimeException e)
    {
        log.error("运行时异常:", e);
        return AjaxResult.error("运行时异常:" + e.getMessage());
    }
    
    /**
     *  校验错误拦截处理
     *
     * @param exception 错误信息集合
     * @return 
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> validationBodyException(MethodArgumentNotValidException exception){

        BindingResult result = exception.getBindingResult();
        StringBuilder sb=new StringBuilder();
        if (result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();

            errors.forEach(p ->{
                FieldError fieldError = (FieldError) p;
                sb.append(fieldError.getDefaultMessage()+",");
            });

        }
        return ResultUtils.WrapError(StringUtils.removeEnd(sb.toString(), ","));
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        return AjaxResult.error("服务器错误，请联系管理员");
    }
    
    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult demoModeException(DemoModeException e)
    {
        return AjaxResult.error("演示模式，不允许操作");
    }

}
