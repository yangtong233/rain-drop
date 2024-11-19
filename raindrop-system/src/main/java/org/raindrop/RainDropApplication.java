package org.raindrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement    //开启事务
@EnableScheduling   //开启定时任务
@EnableAsync    //开启异步任务
public class RainDropApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RainDropApplication.class, args);
    }
}