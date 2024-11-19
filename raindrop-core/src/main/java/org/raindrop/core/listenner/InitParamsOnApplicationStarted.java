package org.raindrop.core.listenner;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动时做一些初始化操作
 */
@Component
@AllArgsConstructor
public class InitParamsOnApplicationStarted implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {

    }
}
