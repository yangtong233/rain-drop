package org.raindrop.core.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * 接口文档配置
 */
@Configuration
public class SpringDocConfig {

    private List<String> swaggerPath;

    @Autowired
    public void autowired(@Value("${server.servlet.context-path}") String contextPath) {
        swaggerPath = List.of(
                contextPath + "/v3/api-docs/**",
                contextPath + "/swagger-ui/**",
                contextPath + "/doc.html",
                contextPath + "/webjars/**",
                contextPath + "/actuator"
        );
    }

    /**
     * 接口文档整体配置
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RainDrop")
                        .description("RainDrop接口文档")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("https://www.4399.com"))
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("233333")
                                .url("https://www.baidu.com")
                )
                .schemaRequirement(HttpHeaders.AUTHORIZATION, securityScheme())
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    @Bean
    public GroupedOpenApi rabcGroup() {
        return GroupedOpenApi.builder()
                .group("RABC")
                .addOpenApiMethodFilter((e) -> e.isAnnotationPresent(Operation.class))
                .pathsToMatch("/rabc/**").build();
    }

    @Bean
    public GroupedOpenApi sysGroup() {
        return GroupedOpenApi.builder()
                .group("系统相关")
                .addOpenApiMethodFilter((e) -> e.isAnnotationPresent(Operation.class))
                .pathsToMatch("/sys/**").build();
    }

    @Bean
    public GroupedOpenApi toolGroup() {
        return GroupedOpenApi.builder()
                .group("系统工具")
                .addOpenApiMethodFilter((e) -> e.isAnnotationPresent(Operation.class))
                .pathsToMatch("/tool/**").build();
    }

    /**
     * 全局请求头
     */
    private SecurityScheme securityScheme() {
        SecurityScheme securityScheme = new SecurityScheme();
        //类型
        securityScheme.setType(SecurityScheme.Type.APIKEY);
        //请求头的name
        securityScheme.setName("Authorization");
        //token所在未知
        securityScheme.setIn(SecurityScheme.In.HEADER);
        return securityScheme;
    }

    /**
     * 获取swagger相关路径，用于排除
     *
     * @return 路径集合
     */
    public List<String> swaggerPath() {
        return swaggerPath;
    }

}
