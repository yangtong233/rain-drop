package org.raindrop.core.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.raindrop.common.exception.BaseException;
import org.raindrop.common.web.R;
import org.raindrop.core.auth.AuthFilter;
import org.raindrop.core.swagger.SpringDocConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * springmvc消息转换器在将控制器的返回值写入响应体之前，有一处增强
 * 在这处增强，可以对最终写入响应体的数据做修改
 * 只能对basePackages指定包下类的返回值生效
 */
@ControllerAdvice
@ConditionalOnBean(AuthFilter.class)
public class RAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private SpringDocConfig springDocConfig;
    AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 根据返回值类型，判断是否进行返回值增强
     * 如果接口返回类型不是R，也不是void，就转换为R再返回
     *
     * @param returnType    封装后的返回值
     * @param converterType 消息转换器
     * @return 是否增强
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
        //如果是swagger请求，则不需要被增强
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BaseException("无法获取请求对象");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        for (String path : springDocConfig.swaggerPath()) {
            if (matcher.match(path, request.getRequestURI())) {
                return false;
            }
        }

        //如果返回的是普通对象，则需要被增强
        return !void.class.equals(returnType.getParameterType())
                && !R.class.equals(returnType.getParameterType());
    }

    /**
     * 对最终写入响应体的返回值进行修改
     *
     * @param body                  控制器方法的返回值
     * @param returnType            封装后的返回值
     * @param selectedContentType   选择的内容类型
     * @param selectedConverterType 选择的转换器类型
     * @param request               请求对象
     * @param response              响应对象
     * @return 返回值就是最终写入响应体的数据
     */
    @Override
    public R<?> beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        return R.success(body);
    }
}
