package org.raindrop.core.mvc.config;

import org.raindrop.core.mvc.interceptor.BaseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.Charset;
import java.util.List;

/**
 * springmvc相关配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BaseInterceptor baseInterceptor;
    //跨域请求配置类
//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        //是否允许请求带有验证信息
//        corsConfiguration.setAllowCredentials(true);
//        // 允许访问的客户端域名
//        corsConfiguration.addAllowedOriginPattern("*");
//        // 允许服务端访问的客户端请求头
//        corsConfiguration.addAllowedHeader("*");
//        // 允许访问的方法名,GET POST等
//        corsConfiguration.addAllowedMethod("*");
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseInterceptor);
    }

    /**
     * 自定义处理器映射器，将来前处理器初始化时会调用initHandlerMappings方法初始化处理器映射器
     * 初始化处理器映射器会优先从容器中得到处理器适配器，如果容器中没有，则使用默认的(默认的会作为前处理器的成员变量)
     */
    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    /**
     * 自定义处理器适配器，将来前处理器初始化时会调用initHandlerAdapters方法初始化处理器适配器
     * 初始化处理器适配器会优先从容器中得到处理器适配器，如果容器中没有，则使用默认的(默认的会作为前处理器的成员变量)
     */
    @Bean
    public RequestMappingHandlerAdapter handlerAdapter(ApplicationContext context) {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        //adapter.setApplicationContext(context);

        //添加默认的参数解析器、返回值解析器、和initBinderArgumentResolvers(这是个啥?)
        //adapter.afterPropertiesSet();

        /**
         *可以给处理器适配器添加自定义的参数解析器和返回值解析器
         */
        //adapter.setCustomArgumentResolvers(null);
        //adapter.setCustomReturnValueHandlers(null);
        adapter.setMessageConverters(List.of(
                new ByteArrayHttpMessageConverter(),
                new StringHttpMessageConverter(Charset.forName("UTF-8")),
                new StringHttpMessageConverter(Charset.forName("ISO-8859-1")),
                new ResourceHttpMessageConverter(),
                new ResourceRegionHttpMessageConverter(),
                new AllEncompassingFormHttpMessageConverter(),
                new MappingJackson2HttpMessageConverter(),
                new Jaxb2RootElementHttpMessageConverter()
        ));
        return adapter;
    }
}