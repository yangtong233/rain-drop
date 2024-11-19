package org.raindrop.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @Description: 非sql日志过滤器
 * @Author: az
 * @Date: 2023-12-21 22:45
 * @Version: 1.0
 */
public class SqlLogFilter extends AbstractMatcherFilter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        return event.getLoggerName().startsWith("sql") ? FilterReply.ACCEPT : FilterReply.DENY;
    }
}
