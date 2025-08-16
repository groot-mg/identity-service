package com.generoso.identity.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.groups.Tuple;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtils {

    public static ListAppender<ILoggingEvent> getListAppenderForClass(Class<?> clazz) {
        var logger = (Logger) LoggerFactory.getLogger(clazz);
        var loggingEventListAppender = new ListAppender<ILoggingEvent>();
        loggingEventListAppender.start();
        logger.addAppender(loggingEventListAppender);
        return loggingEventListAppender;
    }

    public static void assertMessageWasInLogs(ListAppender<ILoggingEvent> appender, String message, Level level) {
        assertThat(appender.list)
                .extracting(ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
                .contains(Tuple.tuple(message, level));
    }
}
