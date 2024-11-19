package org.raindrop.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @Description: 非普通日志过滤器
 * @Author: az
 * @Date: 2023-12-21 22:45
 * @Version: 1.0
 */
public class NormalLogFilter extends AbstractMatcherFilter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        return !event.getLoggerName().startsWith("sql")  &&
                !event.getLevel().toString().equals("ERROR") ? FilterReply.ACCEPT : FilterReply.DENY;
    }
}
