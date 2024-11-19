package org.raindrop.core.mp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus相关配置
 */
//扫描指定包下面的Mapper接口，并生成实现类。@Mapper注解也能实现该功能，@Mapper需要写在每一个Mapper接口上
//这里使用@MapperScan指定了Mapper接口路径，Mapper接口就无需写@Mapper了
@MapperScan("org.raindrop.**.mapper")
@Configuration
public class MybatisPlusConfig {

    /**
     * 给MybatisPlus添加插件
     * @return
     */
    @Bean
    public MybatisPlusInterceptor interceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        //自定义插件
        //interceptor.addInnerInterceptor(new DataPermissionInterceptor());
        //分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        //防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.use(false);
//    }
}
